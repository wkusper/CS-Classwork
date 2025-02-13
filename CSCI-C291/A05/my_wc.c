// Author - will kusper
// my_wc - a program which has several different modes, which report different informations about the inputted file

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>

void printHelp() {
    printf("Prints the word count from stdin.\n");
    printf("Usage: my_wc [-h] [-l] [-c] [-w] [-f] [-d] [-v] [-a] [-p]\n");
    printf("-h -- display this help message\n");
    printf("-d -- debugging output to stderr\n");
    printf("-w -- word count (default with no options)\n");
    printf("-c -- character count\n");
    printf("-l -- line count\n");
    printf("-f -- word frequency\n");
    printf("-v -- output all word, line, character. and frequency\n");
    printf("-a -- report average word and line length\n");
    printf("-p -- do not include punctuation in words\n");
}

typedef struct {
    char *word;
    int frequency;
} wordFrequency;

int main(int argc, char *argv[]) {
    int opt;
    int option_given = 0; // Flag to check if any option is given
    char *input = NULL; // Pointer to the dynamically allocated memory
    size_t size = 40; // Initial size of character storage
    size_t capacity = size; // Current capacity
    size_t word_list_size = 5;
    size_t length = 0; // Length of the input read so far
    int line_count = 0;
    int char_count = 0;
    int word_count = 0;
    char c;

    // Initial allocation
    input = (char*)malloc(size);
    if (input == NULL) {
        fprintf(stderr, "Failed to allocate memory.\n");
        return 1;
    }
    
    wordFrequency *word_list = (wordFrequency*)malloc(word_list_size * sizeof(wordFrequency));
    if (word_list == NULL) {
        fprintf(stderr, "Failed to allocate memory for word list.\n");
        free(input);
        return 1;
    }

    // Reading input
    while ((c = getchar()) != EOF) {
        // Check if the current input length is about to exceed the capacity
        if (length + 1 >= capacity) {
            // Increase capacity by 40 bytes
            capacity += 40;
            // Reallocate memory
            char *temp = realloc(input, capacity);
            if (temp == NULL) {
                fprintf(stderr, "Failed to reallocate memory.\n");
                free(input); // Free the previously allocated memory
                return 1;
            }
            input = temp; // Update the pointer to the new memory block
        }
        // Store the character
        input[length++] = c;
        // increments line_count whenever a newline character is found.
        if (c == '\n') line_count++;
        // increments character counter by 1
        char_count++;
    }

    // Null-terminate the string
    input[length] = '\0';
    
    //tokenize string and count the words
    char *word = strtok(input, " \t\n");
    while (word != NULL) {
        //count the words
        word_count++;
        // Check if the word is already in the word list and update its frequency
        int found = 0;
        for (size_t i = 0; i < word_list_size; i++) {
            if (word_list[i].word != NULL && strcmp(word_list[i].word, word) == 0) {
                word_list[i].frequency++;
                found = 1;
                break;
            }
        }
        if (!found) {
            // Add the word to the word list and set its frequency to 1
            // Reallocate memory for the word list if necessary
            if (word_list_size == 0) {
                word_list_size = 1;
                word_list = realloc(word_list, word_list_size * sizeof(wordFrequency));
            } else {
                word_list_size++;
                word_list = realloc(word_list, word_list_size * sizeof(wordFrequency));
            }
            if (word_list == NULL) {
                fprintf(stderr, "Failed to reallocate memory for word list.\n");
                free(input);
                return 1;
            }
            word_list[word_list_size - 1].word = strdup(word);
            word_list[word_list_size - 1].frequency = 1;
        }
        word = strtok(NULL, " \t\n");
    }





    // Free the allocated memory after reading the input
    free(input);

    while ((opt = getopt(argc, argv, "cwlhdfv")) != -1) {
        option_given = 1; // Set the flag to true when any option is given
        switch (opt) {
            case 'c':
                printf("char count : %d\n", char_count);
                break;
            case 'w':
                printf("word count : %d\n", word_count);
                break;
            case 'l':
                printf("line count : %d\n", line_count);
                break;
            case 'h':
                printHelp();
                break;
            case 'd':
                break;
            case 'v':
                printf("line count : %d\n", line_count);
                printf("char count : %d\n", char_count);
                printf("word count : %d\n", word_count);
                printf("word frequency :\n");
                 // Print the frequency of each word
                 for (size_t i = 0; i < word_list_size; i++) {
                     if (word_list[i].word != NULL) {
                         printf("    %s : %d\n", word_list[i].word, word_list[i].frequency);
                     }
                 }
                break;
            case 'f':
                printf("word frequency :\n");
                // Print the frequency of each word
                for (size_t i = 0; i < word_list_size; i++) {
                    if (word_list[i].word != NULL) {
                        printf("    %s : %d\n", word_list[i].word, word_list[i].frequency);
                    }
                }

                break;
            case '?':
                // getopt will have already printed an error message
                break;
            default:
                printf("word count : %d\n", word_count);
                break;
        }
    }

    // Free the word list memory
    free(word_list);

    return 0;
}
