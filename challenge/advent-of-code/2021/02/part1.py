import sys


horizontal, depth = 0, 0

for line in sys.stdin:
	line = line.strip()

	if not len(line):
		break
	
	action, value = line.split(" ", 2)
	x = int(value)

	if action == "forward":
		horizontal += x
	elif action == "down":
		depth += x
	elif action == "up":
		depth -= x
	else:
		raise ValueError(line)

print(horizontal, depth)
print(horizontal * depth)
