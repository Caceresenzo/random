import sys

SLIDING = 3

values = []
for line in sys.stdin:
	line = line.strip()

	if not len(line):
		break

	values.append(int(line))

previous = None
total_increase = 0

for i in range(len(values) - SLIDING + 1):
	value = sum(values[i:i + SLIDING])

	if previous is not None and previous < value:
		total_increase += 1

	previous = value

print(total_increase)
