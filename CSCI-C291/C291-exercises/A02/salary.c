/* salary.c - Program to compute average salary for different staff
 * Author - Will Kusper
 * Program takes different employee types, their hours, and returns the average salary for each employee type */

#include <stdio.h>
#include <math.h>

void main(void) {
    //constants for usage
    double monthsInSemester = 5;
    double weeksInMonth = 4;
    double monthsInYear = 12;
    double hoursPerWeek = 40;
    double hoursInYear = monthsInYear * weeksInMonth * hoursPerWeek;
    double weeksInSemester = monthsInSemester * weeksInMonth;
    // since theyre paid 8500 per 3 credits. this finds pay per individual credit.

    //counters for each staff type, used to average each salary later
    double adminCounter = 0;
    double staffCounter = 0;
    double hourlyCounter = 0;
    double adjunctCounter = 0;
    double regularCounter = 0;
    double TAcounter = 0;
    
    //total of overtime pay for each staff type, used to calculate actual monthly pay later 
    double staffTotalOT = 0;
    double hourlyTotalPay = 0;
    double regularTotalOT = 0;

    double TAtotalPay = 0;

    //total of expected salary for each staff type, used to average each salary later
    //omitting administrators because they have no additional modifications to pay, such as overtime
    double adminSalary = 0;
    double staffSalary = 0;
    double hourlySalary = 0;
    double adjunctSalary = 0;
    double regularSalary = 0;
    double TAsalary = 0;

    char input;

    do {
    double salary = 0;
    double overtime = 0;
    double hours = 0;
    double credits = 0;
    double courses = 0;
    printf("Enter a payroll code, enter ? for help\n");
    scanf(" %c", &input); // Assigns inputted character to be used in the switch
    switch (input) {
    case '?' : //help case
        printf("Payroll help:\n q/Q : quit\n a/A : Administrator\n s/S : Staff\n h/H : Hourly\n");
        printf(" j/J : Adjunct faculty\n r/R : Regular Faculty\n t/T : Teaching assistants\n");
        break;
    case 'q':
    case 'Q':
        //Quits program
        break;        
    case 'a':
    case 'A':
        {//Administrator calculations here
        printf("Enter Admin Salary: ");
        scanf(" %lf", &salary); //takes inputted salary and maps it to salary for usage
        adminCounter++; //increments adminCounter for averaging at end
        adminSalary += salary;
        break; }
    case 's':
    case 'S':
        {//do staff calculations here
        printf("Enter Staff Salary: ");
        scanf(" %lf", &salary); // takes inputted salary and maps it to salary for usage
        printf("Enter hours of overtime: "); //takes inputted overtime for calculations
        scanf(" %lf", &overtime);
        // if statement checks to see if overtime inputted exceeds the 10 hours allowed per month. if so, sets overtime to 10
        if (overtime > 10) { 
        printf("WARNING : Only 10 hours of overtime pay permitted, discarding additional hours\n");
        overtime = 10;
        }
        staffCounter++; // increments staffTotal for averaging at end
        staffSalary += salary;
        staffTotalOT+= overtime * 1.5 * (salary / hoursInYear);
        break; }
    case 'h':
    case 'H':
        {
        //do hourly calculations here
        //next 4 lines take input to map values to payRate and hours
        printf("Enter pay per hour: ");
        scanf(" %lf", &salary);
        printf("Enter hours worked: ");
        scanf(" %lf", &hours);
        // checks if they have overtime, and if they exceeded the 20 hours of overtime pay allowed for hourly employees
        if (hours > 80) {
            if (hours > 100) {
                printf("WARNING: Only 20 hours of overtime permitted!\n");
                hours = 80;
                overtime = 20;
            }
            else {
                overtime = hours - 80;
                hours = 80;
            }
        }
        //calculates expected salary, then actual pay
        hourlyCounter++; //increments hourly counter
        hourlyTotalPay += (overtime*salary*1.25) + (hours * salary); //adds total pay to running total
        hourlySalary+= hours*salary; // adds expected salary to total
        break; }
    case 'j':
    case 'J':
        {
        //do adjunct faculty calculations here
        adjunctCounter++; // increment adjunct faculty counter
        printf("Enter credits worked: ");
        scanf(" %lf", &credits); // takes inputted number of credits
        adjunctSalary += 8500 * credits / 3; // adds expected salary to total
        break; }
    case 'r':
    case 'R':
        {
        //regular faculty calculations here
        regularCounter++; // increments regular faculty counter
        printf("Enter faculty Salary: ");
        scanf(" %lf", &salary); //takes inputted salary and maps it to salary
        printf("Enter overload credits: ");
        scanf(" %lf", &credits); //takes inputted overload credits and maps it to credits
        regularSalary += salary; // adds expected salary to total
        regularTotalOT += (credits * 8500 / 3) / monthsInSemester; //calculates total pay by adding 
        //salary to overload pay rate. overload pay is for a single semester so calculating that here
        break; }
    case 't':
    case 'T':
        { 
        TAcounter++; // increments TA counter
        printf("Enter number of courses: ");
        scanf(" %lf", &courses);
        printf("Enter hours worked: ");
        scanf(" %lf", &hours);
        
        // calculate pay at base rate
        if (hours > courses * 10 * weeksInMonth) {
            if (hours > courses * 12 * weeksInMonth) {
                printf("WARNING: Only 2 hours of overtime per week per course\n");
                overtime = courses * 2 * weeksInMonth;
                hours = 10 * courses * weeksInMonth;
            }
            else {
                overtime = hours - (courses * 10 * weeksInMonth);
                hours = hours - overtime;
            }
        }
        TAsalary += (2500 * courses) * (hours / (10 * weeksInMonth *  courses));
        TAtotalPay += (2500 * courses) * ((hours + overtime) / (10 * weeksInMonth * courses));

        break;
        }
    default: //handles case of incorrect input
        printf("INVALID INPUT\n");
        break;
        } 
    } while (input != 'q' && input != 'Q'); // Closes loop when Q or q is inputted
    printf("Quitting...\n");
    //Calculates the average pays and salarys of each employee type. admin doesnt different have a pay and salary since they have no additional pay
    double adminAvg = adminSalary / monthsInYear / adminCounter;
    double staffAvgSalary = staffSalary / monthsInYear / staffCounter;
    double hourlyAvgSalary = hourlySalary / hourlyCounter;
    double adjunctAvgSalary= adjunctSalary / monthsInSemester / adjunctCounter;
    double regularAvgSalary = regularSalary / (2 * monthsInSemester) / regularCounter;
    double TAavgSalary = TAsalary / monthsInSemester / TAcounter;

    double staffAvgPay = staffAvgSalary + staffTotalOT / staffCounter;
    double hourlyAvgPay = hourlyTotalPay / hourlyCounter;
    double regularAvgPay = regularAvgSalary + regularTotalOT / regularCounter;
    double TAavgPay = TAtotalPay / monthsInSemester /TAcounter;

    printf("Administrator   %.2lf   %.2lf\n", adminAvg,adminAvg);
    printf("Hourly   %.2lf   %.2lf\n", hourlyAvgSalary, hourlyAvgPay);
    printf("Adjunct   %.2lf   %.2lf\n", adjunctAvgSalary, adjunctAvgSalary);
    printf("Staff   %.2lf   %.2lf\n", staffAvgSalary, staffAvgPay);
    printf("Faculty   %.2lf   %.2lf\n", regularAvgSalary, regularAvgPay);
    printf("TA   %.2lf   %.2lf\n", TAavgSalary, TAavgPay);
}   
