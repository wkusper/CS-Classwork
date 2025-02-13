```
First, define variables for every constant, including hours in a week, months in a year, months in semester,
hours in year
then add counter and two totals for each employee type. One total is for expected pay, while one is for actual pay.
Then create a do while loop
    This loop checks for an inputted employee type code.
    If someone inputs a '?', it lists employee type codes
    If someone enters 'a' or 'A', performs administrator functions
        adds 1 to administrator counter
        adds the inputted salary to administrator totals.
    if someone enters 's' or 'S', performs staff functions
        adds 1 to staff counter
        takes salary and overtime as inputs
        adds inputted salary to expected pay counter
        if there is more than 10 hours of inputted overtime, limits overtime at 10
        adds salary and any overtime pay, which is overtime * 1.5 * (salary / hours in year), to actual pay counter
    if someone enters 'h' or 'H', performs hourly functions
        adds 1 to hourly counter
        takes hours and a wage as inputs
        adds an expected wage of 20 hours * 4 weeks * wage to expected pay total
        checks if there is any overtime through checking hours. If hours are over 80, does hours - 80
        if hours are over 100, sets hours at 80 and overtime at 20
        then adds regular pay and overtime pay, which is overtime * wage * 1.25, to actual pay total
    if someone enters 'j' or 'J', performs adjunct faculty functions
        adds 1 to adjuct faculty counter
        takes inputted number of credits
        checks if its over the limit of 9 credits, if so, limits it at 9
        adds expected pay of 9 credits, which is 3 credits * 8500 per 3 credits, to expected pay total
        then adds actual pay, which is 8500/3 * credits
    if someone enters 's' or 'S', performs regular faculty functions
        adds 1 to regular faculty counter
        takes inputted number of credits and a salary
        checks if the inputted credits is over their limit of 9, if so, limits it at 9
        adds their salary to expected pay total
        adds their overload pay, which is 8500/3 * credits, to their salary, and adds that total to actual pay total
    if someone enters 't' or 'T', performs TA functions
        adds 1 to TA counter
        takes inputted number of classes worked, and hours worked
        checks if there was overtime
            does so by multiplying the number of classes worked by number of weeks in a month, 4, and expected hours            per week, 10.
            if this number is less than total hours worked, subtract actual hours from expected work
                then check if that number is more than the 2 hours per week * classes worked * 4 weeks in a month
                if so, set overtime equal to 2 hours per week * classes worked * 4 weeks in a month
                otherwise, set overtime equal to the difference between hours worked and expected hours
        adds expected salary of 2500 per course to expected pay total
        then calculates actual pay, which is 2500 / (4 weeks in month * 5 months in semester * 10 hours per week),
        and multiply that number by hours worked that month, and add that to actual pay total
    if someone 'q' or 'Q', break the loop
    if someone enters any other input, reprompt them and give them a message telling them the input was invalid
After the loop is broken, calculate the expected pay averages and actual pay averages
expected pay averages below
for Admins, its expected salary total / 12 months in year / number of admins in admin counter
for staff, its expected salary total / 12 months in year / number of staff in staff counter
for hourly, its expected pay total / number of hourly employees in hourly counter
for adjunct faculty, its expected pay total / 5 months in semester / number of adjunct in adjunct counter
for regular faculty, its expected pay total / 5 months in smester / number of regular faculty in counter
for TAs, its expected pay total / 5 months in semester / number of TAs in TA counter
then, calculates actual pay averages
for admins, its the same as expected salary average
for staff, its actual pay total / 12 months in year / number of staff in counter
for hourly, its actual pay total / number of hourly employees in hourly counter
for adjunct faculty, its actual pay total / 5 months in semester / number of adjunct in adjunct counter
for regular faculty, its actual pay total / 5 months in semester / number of regular faculty in counter
for TAs, its actual pay total / 5 months in semester / number of TAs in TA counter
Then, prints each Employee type, their expected avg pay for the month, and their actual avg pay for the month

