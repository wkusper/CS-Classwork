/* fizzbuzz.c - Solution to the fizzbuzz math game
 * Author - Will Kusper
 * Program counts to a given number adhering to the rules of fizzbuzz */

#include <stdio.h>
void main(void) {
    int inputNumber = 0;
    int counter = 0;
    scanf("%d", &inputNumber);

while (counter <= inputNumber) {
        if (counter == 0) {
            printf("%d\n", counter);
        } else if (counter % 3 == 0 && counter % 5 == 0) {
            printf("fizzbuzz\n");
        } else if (counter % 3 == 0) {
            printf("fizz\n");
        } else if (counter % 5 == 0) {
            printf("buzz\n");
        } else {
            printf("%d\n", counter);
        }

        counter++;
    }
}
