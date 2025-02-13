PopulateBuffer
    reads characters inputted
    Limits the number of inputted characters to 2048
    ends early if it receives an EOF
    returns number of bytes entered

IndexBuffer
    declares variables for word length, word start, current index, word count, and if its inside a word
    loops through all elements in the buffer
    check to see if it is inside a word
        if so, checks to see if it encountered an empty space or null
            if so, checks to see if the word repeats
                checks length of previous entries and compares to current word
                    if theyre the same, compare the characters of the words
                        if theyre the same, add 1 to the counter for the previously found word
            if not, sets the current index of the index array to contain the data of its start index, word length, and sets its word counter to 1
    if not, says its inside a word and sets word start to current buffer element
    returns number of unique words

PrintReport
    Prints the line which states the columns
    prints the data of the index array line by line
    sums number of total words including repeats by using the word counter in the index array, element 2
    prints the word in the buffer using the wordStart and wordLength from the array
    prints total number of bytes entered
    prints total number of unique words
    prints total number of words

main
    declares a buffer of size 2048
    declares an index array, which is multidimensional, with dimension 1 having a size of 2048/2, and dimension 2 having a size of 3
    initializes a variable to store number of bytes
    initializes a variable to store number of unique words
    stores number of bytes as return value of populateBuffer
    stores number of unique words as return value of indexBuffer
    calls printReport with index array, buffer array, wordCount, and charCount
