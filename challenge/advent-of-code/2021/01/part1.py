import sys


previous = None

total_increase = 0

for line in sys.stdin:
	line = line.strip()

	if not len(line):
		break

	value = int(line)
	if previous is not None and previous < value:
		total_increase += 1

	previous = value

print(total_increase)
