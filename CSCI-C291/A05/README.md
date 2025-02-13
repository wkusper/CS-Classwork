```Function printHelp():
    Print help message explaining program usage and options

Function main(argc, argv):
    Declare variables:
        opt - to store command line option
        option_given - Flag to check if any option is given
        input - Pointer to the dynamically allocated memory
        size - Initial size of character storage
        capacity -  Current capacity
        word_list_size - Size of word frequency list
        length - Length of the input read so far
        line_count - Count of lines
        char_count - Count of characters
        word_count - Count of words
        c - Temporary character storage
        word_list - List to store words and their frequency

    Allocate memory for input and word_list

    Read input from stdin until EOF:
        Store characters in input
        Update line_count and char_count

    Null-terminate the input string

    Tokenize input string and count words:
        Tokenize input by space, tab, and newline
        For each word:
            Count word occurrence
            If word already exists in word_list:
                Update its frequency
            Otherwise
                Add word to word_list and set frequency to 1

    Free the allocated memory for input

    Parse command line options using getopt:
        Iterate over command line options:
            If option is 'c':
                Print character count
            If option is 'w':
                Print word count
            If option is 'l':
                Print line count
            If option is 'h':
                Call printHelp function
            If option is 'v':
                Print line count, character count, word count
                Print word frequencies
            If option is 'f':
                Print word frequencies
            If option is '?' or default:
                Print word count

    Free the allocated memory for word_list```
