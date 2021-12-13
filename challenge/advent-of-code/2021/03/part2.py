import operator
import sys

lines = []

for line in sys.stdin:
	line = line.strip()

	if not len(line):
		break
	
	lines.append(line)


def filterer(comparator, equal_count_character_resolution):
	filtered_lines = lines.copy()

	for index in range(len(lines[0])):
		zeros, ones = 0, 0
		
		for line in filtered_lines:
			bit = line[index]
	
			if bit == "0":
				zeros += 1
			elif bit == "1":
				ones += 1
			else:
				raise ValueError(bit)
		
		to_keep = equal_count_character_resolution
		if zeros != ones:
			to_keep = "0" if comparator(zeros, ones) else "1"
		
		filtered_lines = list(filter(lambda x: x[index] == to_keep, filtered_lines))
		
		assert len(filtered_lines) != 0
		
		if len(filtered_lines) == 1:
			return int(filtered_lines[0], 2)
	
	raise Exception("invalid state")


oxygen_generator_rating = filterer(operator.__gt__, "1")
co2_scrubber_rating = filterer(operator.__lt__, "0")

print(oxygen_generator_rating, co2_scrubber_rating)
print(oxygen_generator_rating * co2_scrubber_rating)
