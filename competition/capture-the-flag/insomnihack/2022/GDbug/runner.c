#include <stdio.h>

int
main(int argc, char **argv)
{
	const char *input = argv[1]; // "CCCC-CCCC-CCCC-CCCCC-CCCR";

	int serial_index;
	int serial_len = strlen(input);

	int accumulator = 0x539;
	for (serial_index = 0; serial_index < serial_len; serial_index = serial_index + 1)
	{
		accumulator = accumulator + input[serial_index];
		printf("%d %d\n", input[serial_index], accumulator);
	}

	printf("accumulator == 0xb38: %d\n", accumulator == 0xb38);
	printf("serial_len == 0x18: %d\n", serial_len == 0x18);
	printf("input[4] == '-': %d\n", input[4] == '-');
	printf("input[9] == '-': %d\n", input[9] == '-');
	printf("input[0xe] == '-': %d\n", input[0xe] == '-');
	printf("input[0x13] == '-': %d\n", input[0x13] == '-');

	if (accumulator == 0xb38 && serial_len == 0x18 && input[4] == '-' && input[9] == '-' && input[0xe] == '-' && input[0x13] == '-')
	{
		puts("   [-] Registration successful");
		printf("   [-] Your flag is INS{%s}\n\n", input);
	}
	else
	{
		puts("   nope");
	}
}
