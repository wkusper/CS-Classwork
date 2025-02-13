// Author - will kusper
// String_index.c - program to find and report all "words" in an inputted buffer

#include <stdio.h>
#include <ctype.h>

#define BUFFER_SIZE 2048
#define LINE_OF_OUTPUT "%-10d%-10d%-10d"

void populateBuffer(char[]);
void indexBuffer(int[][3], char[]);
void printReport(int[][3], char[]);
int addWord(char[], int[][3], int, int, int);



void populateBuffer(char buffer[]) {
for (int i = 0; i < BUFFER_SIZE; i++) {
		if ((buffer[i] = getchar()) == EOF) {
			i = BUFFER_SIZE;
		}
	}
}

void indexBuffer(int index[][3], char buffer[]) {
    int inWord = 0; //Record whether or not we are currently in a word
	int foundUniqueWords = 0; //How many entries for unique words are in data[][]
	int startPos = -1; //Start position of the current word
	int endPos = -1; //End position of the current word
	for (int i = 0; i < BUFFER_SIZE; i++) {
		if (buffer[i] == -1) {
			if (inWord) {
				endPos = i;
				if (addWord(buffer, index, startPos, endPos, foundUniqueWords)) {
					foundUniqueWords += 1;
				}
			}
			i = BUFFER_SIZE;
		}
		else if (!inWord && !isspace(buffer[i])) { //If we aren't in a word, and the current character isn't whitespace
			inWord = 1;
			startPos = i;
		}
		else if (inWord && isspace(buffer[i])) { //If we are in a word and the current charater is whitespace
			inWord = 0;
			endPos = i;
			if (addWord(buffer, index, startPos, endPos, foundUniqueWords)) {
				foundUniqueWords += 1;
			}
		}
		// The other cases both do nothing and scan the next character
	}
	index[foundUniqueWords][0] = -1;
   
}

void printReport(int index[][3], char buffer[]) {
	int i =  0; // counting variable for the loop. Also used to record number of unique words found
	int totalWords = 0; //Total number of non-unique words found
	int totalBytes = 0; //Total number of bytes of data analyzed
	for (int j = 0; j < BUFFER_SIZE; j++) {
		if (buffer[j] != -1) {
			totalBytes += 1;
		}
		else {
			j = BUFFER_SIZE;
		}
	}
	printf("BEGIN     LENGTH    COUNT     WORD\n");
	while (index[i][0] != -1) {
		printf(LINE_OF_OUTPUT, index[i][0], index[i][1], index[i][2]);
		for (int j = index[i][0]; j < index[i][0] + index[i][1]; j++) {
			printf("%c", buffer[j]);
		}
		printf("\n");
		totalWords += index[i][2];
		i++;
	}
	printf("Total bytes processed: %d\n"
		"Total Unique Words: %d\n"
		"Total words found: %d\n", totalBytes, i, totalWords);
	fflush(stdout);
}

int addWord(char buffer[], int index[][3], int startPos, int endPos, int foundUniqueWords) {
	int foundWord = 1; //Whether or not the current word being tested is unique. Starts as true
	for (int i = 0; i < foundUniqueWords; i++) {
		if (endPos - startPos == index[i][1]) {
			for (int j = 0; j < (endPos - startPos); j++) {
				if (buffer[index[i][0] + j] != buffer[startPos + j]) {
					foundWord = 0;
					j = endPos - startPos;
				}
			}
			if (foundWord) {
				index[i][2]++;
				return 0;
			}
		}
		foundWord = 1;
	}
	index[foundUniqueWords][0] = startPos;
	index[foundUniqueWords][1] = endPos - startPos;
	index[foundUniqueWords][2] = 1;
	return 1;
}


int main(void) {
// initializes both buffer an index, and declares max size for buffers
    char buffer[BUFFER_SIZE];
    int index[BUFFER_SIZE / 2][3] = {{0, 0, 0}, {-1, 0, 0}};

	populateBuffer(buffer);

	indexBuffer(index, buffer);

	printReport(index, buffer);

	return 0;
}
