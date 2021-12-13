import sys


horizontal, depth, aim = 0, 0, 0

for line in sys.stdin:
	line = line.strip()

	if not len(line):
		break
	
	action, value = line.split(" ", 2)
	x = int(value)

	if action == "forward":
		horizontal += x
		depth += aim * x
	elif action == "down":
		aim += x
	elif action == "up":
		aim -= x
	else:
		raise ValueError(line)

print(horizontal, depth)
print(horizontal * depth)
