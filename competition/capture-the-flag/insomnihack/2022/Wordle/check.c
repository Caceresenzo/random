#include <stdio.h>

int
main(int argc, char **argv)
{
	int dead = 0xdeadbeef;
	char *ptr = (char*)&dead;

//	printf("%c%c%c%c", ptr[3], ptr[2], ptr[1], ptr[0]);

//	char buff[] = { 0xde, 0xad, 0xbe, 0xef };
	char buff[] = { 0xef, 0xbe, 0xad, 0xde };
	int beef = *((int*)((char*)buff));

	printf("%d\n", beef);
	printf("%x\n", beef);
}
