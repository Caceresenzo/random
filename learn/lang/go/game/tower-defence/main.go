package main

import (
	"container/list"
	"fmt"
	"github.com/hajimehoshi/ebiten"
	"github.com/hajimehoshi/ebiten/audio"
	"github.com/hajimehoshi/ebiten/audio/mp3"
	"github.com/hajimehoshi/ebiten/ebitenutil"
	"image/color"
	"math"
)

type Point2i struct {
	x int
	y int
}

type Point2f struct {
	x float64
	y float64
}

type PathNode struct {
	pos  Point2i
	end  bool
	next *PathNode
}

type Enemy struct {
	pos         Point2f
	health      int
	startNode   *PathNode
	endNode     *PathNode
	audioPlayer *audio.Player
}

type Tower struct {
	pos           Point2i
	cooldown      int
	value         int
	maxRange      float64
	lastFire      Point2f
	lastFireDecay byte
	audioPlayer   *audio.Player
}

type Generator struct {
	every int
	value int
}

const (
	pixelSize = 20
)

var (
	generator = Generator{60, 0}

	nodes   *list.List
	enemies *list.List
	towers  *list.List

	money = 500

	mousePressed = false

	blockImage  *ebiten.Image
	dotImage    *ebiten.Image
	ghostImage  *ebiten.Image
	pacmanImage *ebiten.Image

	audioContext   *audio.Context
	decodedPewFile *mp3.Stream
	audioPlayer    *audio.Player
	fireSoundFile  ebitenutil.ReadSeekCloser
)

func update(screen *ebiten.Image) error {
	{
		for e := nodes.Front(); e != nil; e = e.Next() {
			node := e.Value.(*PathNode)

			screen.DrawImage(blockImage, &ebiten.DrawImageOptions{GeoM: ebiten.TranslateGeo(float64((node.pos.x)*pixelSize), float64(node.pos.y*pixelSize))})

			if !node.end {
				var half = pixelSize / 2
				var x1 = float64((node.pos.x)*pixelSize + half)
				var y1 = float64((node.pos.y)*pixelSize + half)
				var x2 = float64((node.next.pos.x)*pixelSize + half)
				var y2 = float64((node.next.pos.y)*pixelSize + half)

				ebitenutil.DrawLine(screen, x1, y1, x2, y2, color.RGBA{0x77, 0x6e, 0x65, 0xff})
			} else {
				var x1 = float64((node.pos.x) * pixelSize)
				var y1 = float64((node.pos.y) * pixelSize)

				ebitenutil.DrawRect(screen, x1, y1, pixelSize, pixelSize, color.RGBA{0x77, 0x6e, 0x65, 0xff})
			}
		}
	}

	{
		generator.value--

		if generator.value == -1 {
			n := nodes.Front().Value.(*PathNode)

			f, err := ebitenutil.OpenFile("assets/explosion.mp3")
			if err != nil {
				panic(err)
			}

			d, err := mp3.Decode(audioContext, f)
			if err != nil {
				panic(err)
			}

			audioPlayer, err := audio.NewPlayer(audioContext, d)
			if err != nil {
				panic(err)
			}

			enemies.PushBack(&Enemy{Point2f{float64(n.pos.x), float64(n.pos.y)}, 5, n, n.next, audioPlayer})

			generator.value = generator.every
		}
	}

	{
		for e := enemies.Front(); e != nil; e = e.Next() {
			enemy := e.Value.(*Enemy)

			var dx = float64(enemy.endNode.pos.x - enemy.startNode.pos.x)
			var dy = float64(enemy.endNode.pos.y - enemy.startNode.pos.y)

			var distance = math.Sqrt(dx*dx + dy*dy)
			var scale = float64(0.03 / distance)

			enemy.pos.x += dx * scale
			enemy.pos.y += dy * scale

			screen.DrawImage(ghostImage, &ebiten.DrawImageOptions{GeoM: ebiten.TranslateGeo(float64((enemy.pos.x)*pixelSize), float64(enemy.pos.y*pixelSize))})

			if math.Abs(float64(int(enemy.pos.x*10)-enemy.endNode.pos.x*10)) < 1 && math.Abs(float64(int(enemy.pos.y*10)-enemy.endNode.pos.y*10)) < 1 {
				enemy.startNode = enemy.endNode
				enemy.endNode = enemy.endNode.next
			}
		}
	}

	{
		for e := towers.Front(); e != nil; e = e.Next() {
			tower := e.Value.(*Tower)

			screen.DrawImage(pacmanImage, &ebiten.DrawImageOptions{GeoM: ebiten.TranslateGeo(float64((tower.pos.x)*pixelSize), float64(tower.pos.y*pixelSize))})

			tower.value--
			if tower.value == -1 {
				fired := false

				for ee := enemies.Front(); ee != nil; ee = ee.Next() {
					enemy := ee.Value.(*Enemy)

					var dx = enemy.pos.x - float64(tower.pos.x)
					var dy = enemy.pos.y - float64(tower.pos.y)

					var distance = math.Sqrt(dx*dx + dy*dy)

					if distance < tower.maxRange {
						tower.lastFire = Point2f(enemy.pos)
						tower.lastFireDecay = 255

						enemy.health--
						if enemy.health == 0 {
							enemy.audioPlayer.Rewind()
							enemy.audioPlayer.Play()

							enemies.Remove(ee)
							money += 50
						}

						tower.audioPlayer.Rewind()
						tower.audioPlayer.Play()

						fired = true
						break
					}
				}

				if fired {
					tower.value = tower.cooldown
				} else {
					tower.value = 0
				}
			}

			if tower.lastFireDecay != 0 {
				var half = float64(pixelSize / 2)
				var x1 = float64(float64(tower.pos.x)*pixelSize + half)
				var y1 = float64(float64(tower.pos.y)*pixelSize + half)
				var x2 = float64((tower.lastFire.x)*pixelSize + half)
				var y2 = float64((tower.lastFire.y)*pixelSize + half)

				ebitenutil.DrawLine(screen, x1, y1, x2, y2, color.RGBA{0x77, 0x6e, 0x65, tower.lastFireDecay})

				tower.lastFireDecay -= 10
				if tower.lastFireDecay < 20 {
					tower.lastFireDecay = 0
				}
			}
		}
	}

	{
		mouseX, mouseY := ebiten.CursorPosition()
		ebitenutil.DebugPrint(screen, fmt.Sprintf("X: %d, Y: %d", mouseX/pixelSize, mouseY/pixelSize))
		ebitenutil.DebugPrintAt(screen, fmt.Sprintf("$ %d", money), 0, 15)

		if !mousePressed && ebiten.IsMouseButtonPressed(ebiten.MouseButtonLeft) {
			mousePressed = true

			if money >= 200 {
				f, err := ebitenutil.OpenFile("assets/pew.mp3")
				if err != nil {
					panic(err)
				}

				d, err := mp3.Decode(audioContext, f)
				if err != nil {
					panic(err)
				}

				audioPlayer, err := audio.NewPlayer(audioContext, d)
				if err != nil {
					panic(err)
				}

				money -= 200
				towers.PushFront(&Tower{Point2i{mouseX / pixelSize, mouseY / pixelSize}, 30, 0, 8.0, Point2f{0.0, 0.0}, 0, audioPlayer})
			}
		}

		mousePressed = ebiten.IsMouseButtonPressed(ebiten.MouseButtonLeft)
	}

	return nil
}

func loadImage(file string) *ebiten.Image {
	img, _, err := ebitenutil.NewImageFromFile(file, ebiten.FilterDefault)

	if err != nil {
		panic(err)
	}

	return img
}

func main() {
	blockImage = loadImage("assets/border.png")
	dotImage = loadImage("assets/dot.png")
	ghostImage = loadImage("assets/ghost.png")
	pacmanImage = loadImage("assets/pac-up1.png")

	var err error

	audioContext, err = audio.NewContext(48000)
	if err != nil {
		panic(err)
	}

	nodes = list.New()
	enemies = list.New()
	towers = list.New()

	var n *PathNode
	n = &PathNode{Point2i{19, 12}, true, nil}
	nodes.PushFront(n)
	n = &PathNode{Point2i{16, 18}, false, n}
	nodes.PushFront(n)
	n = &PathNode{Point2i{10, 14}, false, n}
	nodes.PushFront(n)
	n = &PathNode{Point2i{12, 10}, false, n}
	nodes.PushFront(n)
	n = &PathNode{Point2i{10, 4}, false, n}
	nodes.PushFront(n)
	n = &PathNode{Point2i{4, 4}, false, n}
	nodes.PushFront(n)

	if err := ebiten.Run(update, 500, 480, 1, "Hello world!"); err != nil {
		panic(err)
	}
}
