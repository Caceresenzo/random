import sys

lines = []

for line in sys.stdin:
	line = line.strip()

	if not len(line):
		break
	
	lines.append(line)

gama_rate, epsilon_date = 0, 0

for index in range(len(lines[0])):
	zeros, ones = 0, 0
	
	for line in lines:
		bit = line[index]

		if bit == "0":
			zeros += 1
		elif bit == "1":
			ones += 1
		else:
			raise ValueError(bit)
	
	assert zeros != ones
	
	gama_rate <<= 1
	epsilon_date <<= 1
	
	if zeros > ones:
		gama_rate |= 1
	else:
		epsilon_date |= 1

print(gama_rate, epsilon_date)
print(gama_rate * epsilon_date)
