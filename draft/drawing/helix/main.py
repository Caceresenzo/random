import math
import sys
import pygame

pygame.init()
width, height = (400, 600)
surface = pygame.display.set_mode((width * 2, height * 2))
pygame.display.set_caption("Hello World!")

WHITE = 0xFFFFFF
RED = 0xFF0000
GREEN = 0x00FF00
BLUE = 0x8888FF
YELLOW = 0xFFFF00

bars_config = {
    # 0
    30: [RED, RED],
    60: [GREEN, BLUE],
    90: [BLUE, RED],
    120: [RED, GREEN],
    150: [GREEN, RED, GREEN],
    # 180
    210: [GREEN, RED, BLUE],
    240: [BLUE, RED],
    270: [RED, GREEN],
    300: [GREEN, RED],
    330: [GREEN, GREEN]
    # 360
}


offset_y = 0
half = width // 2

while True:
    surface.fill((0, 0, 0))

    for event in pygame.event.get():
        if event.type == pygame.QUIT:
            pygame.quit()
            sys.exit()

    for y in range(0, height):
        degrees = (y + offset_y) % 360

        x = int(math.sin(math.radians(degrees)) * 50)
        left = half + x
        right = half - x

        surface.set_at((left, y), WHITE)
        surface.set_at((right, y), WHITE)

        if degrees in bars_config:
            bars = bars_config[degrees]

            distance = right - left
            bar_length = distance / len(bars)

            for index, bar in enumerate(bars):
                start = (left + (bar_length * index), y)
                end = (left + (bar_length * (index + 1)), y)
                pygame.draw.line(surface, bar, start, end, 1)

        if degrees == 0:
            pygame.draw.line(surface, YELLOW, (0, y), (width, y), 1)

    # pretty bad way to achieve pixel density...
    screenshot = pygame.Surface((width, height))
    screenshot.blit(surface.subsurface((0, 0, width, height)), (0, 0))
    x2 = pygame.transform.scale2x(screenshot)
    surface.blit(x2, (0, 0))

    pygame.display.update()

    offset_y += 1
