// Author - Will Kusper
// Description - program to simplify calculations of employee pay and government withholdings.

#include <stdio.h>
#include <string.h>
#include <math.h>

//general function to calculate withholdings. to call for a specific withholding, the code will call with a char
//for the withholding type and the pay. 'f' is federal withholding, 's' is state withholding, 'o' is social security
//contribution
double withholdings(char withholding, double pay) {
    double result;
    double federal = .15;
    double state = .05;
    double socialSecurity = .075;

    switch(withholding) {
        case 'f':
            result = pay * federal;
            break;
        case 's':
            result = pay * state;
            break;
        case 'o':
            result = pay * socialSecurity;
            break;
    }
    return result;
}
//function to change int for month inputted into a string
char* intToMonth(int month) {
    char *months[] = {"JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE",
                      "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER"};
    return months[month - 1];
}
double totalBonus(double tenure) {
    double decades = (int) tenure / 10;
    double bonusPercent = .1 + (decades * .05);
}

void main(void) {
    int month;
    int year;
    double tenure;
    double sales;
    double totalMonthlySales;
    double totalCommission = 0;
    double totalBonus;
    double totalFedWithholding;
    double totalStateWithholding;
    double totalFica;
    double totalNetPay;

    //dividers copied from assignment 3 instructions
    char divider1[] = "===============================================================================";
    char divider2[] = "-------------------------------------------------------------------------------";
    char labelRow[] = "Name       Commission   Bonus       Fed W.       ST W.       FICA      Net Pay";

    //hardcoded header lines for simplicity later
    char companyName[] = "Bloomington Skating Company";
    char location[] = "Bloomington, IN";
    char phone[] = "Phone: (812)-855-0000";

    double salesArray[5];
    double tenuresArray[5];
    double totalProfitPercentage = .45;
    double employeeEarningPercentage = .125;
    
    while (1) {
        printf("Enter the month 1-12: ");
        scanf("%d", &month);
        if (month < 1 || month > 12) {
            printf("ERROR: INVALID MONTH INPUT\n");
            continue;
        }
        if (month >= 1 && month <= 12) {
            break;
        }
    }
    printf("Enter the year: ");
    scanf("%d", &year);
    
    for (int i = 0; i < 5; i++) {
        printf("Enter either 0 if no employee or enter sales: ");
        scanf("%lf", &sales);
        salesArray[i] = sales;
        totalMonthlySales += sales;
        printf("Enter either 0 if no employee or enter tenure: ");
        scanf("%lf", &tenure);
        tenuresArray[i] = tenure;
    } 
    printf("MONTHLY STATEMENT (%s %d)\n", intToMonth(month), year);
    int companyNameSpacing = sizeof(divider1) - sizeof(companyName);
    printf("%*s%s\n", companyNameSpacing, "", companyName);
    int locationSpacing = sizeof(divider1) - sizeof(location);
    printf("%*s%s\n", locationSpacing, "", location);
    int phoneSpacing = sizeof(divider1) - sizeof(phone);
    printf("%*s%s\n", phoneSpacing, "", phone);
    
    double totalMonthlyProfit = totalMonthlySales * totalProfitPercentage;
    printf("MONTHLY SALES: $%.2lf\n", totalMonthlySales);
    printf("MONTHLY PROFIT: $%.2lf\n", totalMonthlyProfit);
    printf("%s\n", divider1);
    printf("%s\n", labelRow);
    printf("%s\n", divider2);
    for (int i = 0; i < 5; i++) {
        // loop for employee names
        char* name = ""; 
        switch (i) {
            case 0: name = "Empl A:"; break;
            case 1: name = "Empl B:"; break;
            case 2: name = "Empl C:"; break;
            case 3: name = "Empl D:"; break;
            case 4: name = "Empl E:"; break;
        }

        // Calculate commission
        double commission = salesArray[i] * employeeEarningPercentage;
        totalCommission += commission;
        // Calculate full decades and bonus
        double decades = (int)tenuresArray[i] / 10;
        double bonusPercentage = 0.1 + decades * 0.05;
        double bonus = bonusPercentage * totalMonthlyProfit;
        totalBonus += bonus;
        // Calculate withholdings
        double fedWithholding = withholdings('f', (commission + bonus));
        totalFedWithholding += fedWithholding;
        double stateWithholding = withholdings('s', (commission + bonus));
        totalStateWithholding += stateWithholding;
        double fica = withholdings('o', (commission + bonus));
        totalFica +=fica;
        // Calculate net pay
        double netPay = commission + bonus - fedWithholding - stateWithholding - fica;
        totalNetPay += netPay;

        // Print the line with dynamic values
        printf("%-10s %-12.2lf %-11.2lf %-12.2lf %-11.2lf %-9.2lf %.2lf\n",
               name, commission, bonus, fedWithholding, stateWithholding, fica, netPay);
    }
    printf("%s\n", divider2);
    
    printf("Total      %-12.2lf %-11.2lf %-12.2lf %-11.2lf %-9.2lf %.2lf\n", totalCommission, totalBonus, totalFedWithholding, totalStateWithholding, totalFica, totalNetPay);
    printf("%s\n", divider2);
    printf("Net Profit (Profit - Bonus): %.2lf\n", (totalMonthlyProfit - totalBonus));
    printf("John Doe, Branch Manager\n");
}
