// Program to generate 10 characters for a Sci-fi RPG
// Author -- Will Kusper

#include <ctype.h>
#include <math.h>
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <time.h>

#define LINE_OF_OUTPUT "%06d %-4s %-5s | %-2d %-2d %-2d %-2d %-2d | %-2d %-2d %-2d %-2d %-2d | %-4s %-s\n"
#define LINE_OF_INPUT "%06d %4s %5s | %2d %2d %2d %2d %2d | %2d %2d %2d %2d %2d | %4s %s\n"
#define NUMBER_OF_CREW_MEMBERS 10
#define NUMBER_OF_LINES_OF_HEADER 3
#define MAXIMUM_RANK_LENGTH 4
#define MAXIMUM_POST_LENGTH 4
#define MAXIMUM_RACE_LENGTH 4
#define MAXIMUM_NAME_LENGTH 101
#define INITIAL_NAME_LENGTH 10
#define DATA_FILE "crewmember_list.txt"

enum Race {
    TERRAN,
    MARTIAN,
    VENUSIAN,
    SATURIAN
};

enum Post {
    CREWMAN,
    COMMUNICATIONS_OFFICER,
    NAVIGATION_OFFICER,
    ENGINEERING_OFFICER,
    SECURITY_OFFICER,
    CAPTAIN
};

enum Rank {
    SPACER,
    ENSIGN,
    LIEUTENANT,
    COMMANDER
};

typedef struct abilities {
    unsigned int charisma;
    unsigned int intelligence;
    unsigned int strength;
    unsigned int psionics;
    unsigned int agility;
} Abilities;

typedef struct skills {
    signed int navigation;
    signed int engineering;
    signed int tactics;
    signed int leadership;
    signed int diplomacy;
} Skills;

typedef struct crewMember {
    char *name;
    enum Race race;
    Abilities abilities;
    Skills skills;
    void *captain;
    enum Post post;
    enum Rank rank;
    int serviceNumber;
} CrewMember;

int rollDice(int);
void generateCrewMember(CrewMember[], int, int);
void changeName(CrewMember[], int);
void changeCaptain(CrewMember[], int);
void assignPost(CrewMember[], int);
void displayData(CrewMember[], FILE *);
void loadData(CrewMember[]);
void parsePost(CrewMember*, char*);
void parseRank(CrewMember*, char*);
void parseRace(CrewMember*, char*);
int isUnique(CrewMember[], int);
int locateCrewMember(CrewMember[], int);
int highestRank(CrewMember[], int);
char * getRace(CrewMember);
char * getPost(CrewMember);
char * getRank(CrewMember);

// Main function of the program
int main(int argc, char *argv[]) {
	int finishedSettingUp = 0; //Flag for if we continue looping input
	int serviceNumber = 0; //current crew member being specified for a command
	FILE *file = NULL;
	CrewMember *crew = calloc(NUMBER_OF_CREW_MEMBERS, sizeof(CrewMember));
	if (crew == NULL) {
		fprintf(stderr, "[ERROR] Not enough memory for crew\n");
		fflush(stderr);
		exit(EXIT_FAILURE);
	}
	srand(time(NULL));
	for (int i = 0; i < argc; i++) { //check for if a pre-defined seed was given
		if (atoi(*(argv + i)) != 0) {
			fprintf(stderr, "[DEBUG] Seed initialized to %d\n", atoi(*(argv + i)));
			fflush(stderr);
			srand(atoi(*(argv + i)));
		}
	}

	for (int i = 0; i < NUMBER_OF_CREW_MEMBERS; i++) { //Generate the crew
		generateCrewMember(crew, i, 1);
	}

	printf("Please enter an option from the following.\n"
		"The '#' character indicates that a service number must be provided to indicate which crew member will be affected by the command.\n"
		"Options:\n"
		"- (D) Redisplay the table of crewmembers.\n"
		"- (N #) Provide a name for the specified crewmember.\n"
		"- (C #) Make the specified crewmember the captain for the crew. (No other crewmember should have a higher rank than the captain.)\n"
		"- (B #) Assign the specified crewmember to a bridge position by specifying a menu of bridge crew positions.\n"
		"- (S) Save the crewmember list to a file crewmember_list.txt\n"
		"- (R #) Regenerate the given crewmember.\n"
		"- (P) Allows the user to play the game which for your program will result in exiting the program.\n\n");
	fflush(stdout);

	while (!finishedSettingUp) { //main loop to check input
		switch(getchar()) {
			case 'D':
			case 'd':
			displayData(crew, stdout);
			break;
			case 'N':
			case 'n':
			scanf("%d", &serviceNumber);
			changeName(crew, serviceNumber);
			break;
			case 'C':
			case 'c':
			scanf("%d", &serviceNumber);
			changeCaptain(crew, serviceNumber);
			break;
			case 'B':
			case 'b':
			scanf("%d", &serviceNumber);
			assignPost(crew, serviceNumber);
			break;
			case 'S':
			case 's':
			file = fopen(DATA_FILE, "w");
			if (file == NULL) {
				fprintf(stderr, "[ERROR] Could not open file 'crewmember_list.txt'\n");
				fflush(stderr);
				exit(EXIT_FAILURE);
			}
			displayData(crew, file);
			fclose(file);
			break;
			case 'R':
			case 'r':
			scanf("%d", &serviceNumber);
			int indexOfMember = locateCrewMember(crew, serviceNumber);
			if (indexOfMember == -1) {
				break;
			}
			generateCrewMember(crew, indexOfMember, 0);
			break;
			case 'P':
			case 'p':
			finishedSettingUp = 1;
			break;
			case 'L':
			case 'l':
			loadData(crew);
			break;
			default:
			break;
		}
		printf("\n");
		fflush(stdout);
	}
	
	fprintf(stderr, "[DEBUG] FREEING MEMORY\n");
	free(crew);
	exit(EXIT_SUCCESS);
}

// Function to roll dice
int rollDice(int numberOfSides) {
    return (rand() % numberOfSides) + 1;
}

// Function to generate a crew member
void generateCrewMember(CrewMember crew[], int i, int generateServiceNumber) {
	crew[i].name = calloc(sizeof(char), INITIAL_NAME_LENGTH);
	fprintf(stderr, "[DEBUG] Allocating memory\n");
	fflush(stderr);
	if (crew[i].name == NULL) {
		fprintf(stderr, "[ERROR] Not enough memory for name\n");
		fflush(stderr);
		exit(EXIT_FAILURE);
	}
	sprintf(crew[i].name, "Crewman%d", i+1);
	crew[i].post = CREWMAN;
	//Assign ability scores
	crew[i].abilities.charisma = rollDice(6) + rollDice(6) + rollDice(6);
	crew[i].abilities.intelligence = rollDice(6) + rollDice(6) + rollDice(6);
	crew[i].abilities.strength = rollDice(6) + rollDice(6) + rollDice(6);
	crew[i].abilities.psionics = rollDice(6) + rollDice(6) + rollDice(6);
	crew[i].abilities.agility = rollDice(6) + rollDice(6) + rollDice(6);
	switch(rollDice(4)) { //Assign race using a d4
		case 1:
		crew[i].race = TERRAN;
		break;
		case 2:
		crew[i].race = MARTIAN;
		crew[i].abilities.psionics += 2;
		crew[i].abilities.agility += 1;
		crew[i].abilities.strength -= 2;
		break;
		case 3:
		crew[i].race = VENUSIAN;
		crew[i].abilities.charisma += 1;
		crew[i].abilities.intelligence -= 1;
		crew[i].abilities.strength += 1;
		crew[i].abilities.psionics += 1;
		crew[i].abilities.agility -= 1;
		break;
		case 4:
		crew[i].race = SATURIAN;
		crew[i].abilities.charisma -= 1;
		crew[i].abilities.intelligence += 2;
		crew[i].abilities.strength -= 1;
		crew[i].abilities.psionics += 1;
		break;
		default:
		fprintf(stderr, "[ERROR] Crewmember assigned invalid race");
		fflush(stderr);
		exit(EXIT_FAILURE);
	}
	switch(rollDice(6) + rollDice(6)) { //Assign rank using 2d6
		case 2:
		case 3:
		case 4:
		case 5:
		case 6:
		case 7:
		crew[i].rank = SPACER;
		break;
		case 8:
		case 9:
		case 10:
		crew[i].rank = ENSIGN;
		break;
		case 11:
		crew[i].rank = LIEUTENANT;
		break;
		case 12:
		crew[i].rank = COMMANDER;
		break;
		default:
		fprintf(stderr, "[ERROR] Crewmember assigned invalid rank");
		fflush(stderr);
		exit(EXIT_FAILURE);
	}
	switch((int) (crew[i].abilities.intelligence + crew[i].abilities.psionics)/2) {
		case 3:
		crew[i].skills.navigation = -2;
		break;
		case 4:
		case 5:
		case 6:
		crew[i].skills.navigation = -1;
		break;
		case 7:
		case 8:
		case 9:
		case 10:
		case 11:
		case 12:
		case 13:
		case 14:
		crew[i].skills.navigation = 0;
		break;
		case 15:
		case 16:
		case 17:
		crew[i].skills.navigation = 1;
		break;
		case 18:
		crew[i].skills.navigation = 2;
		break;
		default:
		fprintf(stderr, "[ERROR] Crewmember assigned invalid navigation skill");
		fflush(stderr);
		exit(EXIT_FAILURE);
	}
	switch((int) (crew[i].abilities.strength + crew[i].abilities.agility)/2) {
		case 3:
		crew[i].skills.tactics = -2;
		break;
		case 4:
		case 5:
		case 6:
		crew[i].skills.tactics = -1;
		break;
		case 7:
		case 8:
		case 9:
		case 10:
		case 11:
		case 12:
		case 13:
		case 14:
		crew[i].skills.tactics = 0;
		break;
		case 15:
		case 16:
		case 17:
		crew[i].skills.tactics = 1;
		break;
		case 18:
		crew[i].skills.tactics = 2;
		break;
		default:
		fprintf(stderr, "[ERROR] Crewmember assigned invalid tactics skill");
		fflush(stderr);
		exit(EXIT_FAILURE);
	}
	switch((int) (crew[i].abilities.intelligence + crew[i].abilities.charisma)/2) {
		case 3:
		crew[i].skills.leadership = -2;
		break;
		case 4:
		case 5:
		case 6:
		crew[i].skills.leadership = -1;
		break;
		case 7:
		case 8:
		case 9:
		case 10:
		case 11:
		case 12:
		case 13:
		case 14:
		crew[i].skills.leadership = 0;
		break;
		case 15:
		case 16:
		case 17:
		crew[i].skills.leadership = 1;
		break;
		case 18:
		crew[i].skills.leadership = 2;
		break;
		default:
		fprintf(stderr, "[ERROR] Crewmember assigned invalid leadership skill");
		fflush(stderr);
		exit(EXIT_FAILURE);
	}
	switch((int) (crew[i].abilities.charisma + crew[i].abilities.psionics)/2) {
		case 3:
		crew[i].skills.diplomacy = -2;
		break;
		case 4:
		case 5:
		case 6:
		crew[i].skills.diplomacy = -1;
		break;
		case 7:
		case 8:
		case 9:
		case 10:
		case 11:
		case 12:
		case 13:
		case 14:
		crew[i].skills.diplomacy = 0;
		break;
		case 15:
		case 16:
		case 17:
		crew[i].skills.diplomacy = 1;
		break;
		case 18:
		crew[i].skills.diplomacy = 2;
		break;
		default:
		fprintf(stderr, "[ERROR] Crewmember assigned invalid diplomacy skill");
		fflush(stderr);
		exit(EXIT_FAILURE);
	}
	switch((int) (crew[i].abilities.intelligence + crew[i].abilities.agility)/2) {
		case 3:
		crew[i].skills.engineering = -2;
		break;
		case 4:
		case 5:
		case 6:
		crew[i].skills.engineering = -1;
		break;
		case 7:
		case 8:
		case 9:
		case 10:
		case 11:
		case 12:
		case 13:
		case 14:
		crew[i].skills.engineering = 0;
		break;
		case 15:
		case 16:
		case 17:
		crew[i].skills.engineering = 1;
		break;
		case 18:
		crew[i].skills.engineering = 2;
		break;
		default:
		fprintf(stderr, "[ERROR] Crewmember assigned invalid engineering skill");
		fflush(stderr);
		exit(EXIT_FAILURE);
	}
	if (generateServiceNumber) { //If regenerating a crew member, this shouldn't happen
		do {
			crew[i].serviceNumber = rand() % (int) pow(10, 6); //10^6, so the remainder will be a 6 digit number
		} while(!isUnique(crew, i));
	}
}

// Function to change the name of a crew member
void changeName(CrewMember crew[], int serviceNumber) {
	char *s;
	int indexOfMember = locateCrewMember(crew, serviceNumber);
	if (indexOfMember == -1) {
		return;
	}
	fprintf(stderr, "[DEBUG] CHANGING NAME OF CREW MEMBER #%06d\n", serviceNumber);
	fflush(stderr);
	s = calloc(MAXIMUM_NAME_LENGTH, sizeof(char));
	fprintf(stderr, "[DEBUG] ALLOCATING MEMORY\n");
	fflush(stderr);
	printf("Please enter a name for the crew member.\nNames over 100 characters will be truncated after 100 characters.\n");
	scanf("%100s", s);
	if (s == NULL) {
		fprintf(stderr, "[ERROR] Not enough memory for name\n");
		fflush(stderr);
		exit(EXIT_FAILURE);
	}
	s = realloc(s, strlen(s) + 1);
	fprintf(stderr, "[DEBUG] REALLOCATING MEMORY\n");
	fflush(stderr);
	if (s == NULL) {
		fprintf(stderr, "[ERROR] Not enough memory for name\n");
		fflush(stderr);
		exit(EXIT_FAILURE);
	}
	fprintf(stderr, "[DEBUG] FREEING MEMORY\n");
	free(crew[indexOfMember].name);
	crew[indexOfMember].name = s;
}

// Function to change the captain of the crew
void changeCaptain(CrewMember crew[], int serviceNumber) {
    int indexOfMember = locateCrewMember(crew, serviceNumber);
	char *s = calloc(INITIAL_NAME_LENGTH, sizeof(char));
	fprintf(stderr, "[DEBUG] Allocating memory\n");
	if (s == NULL) {
		fprintf(stderr, "[ERROR] Not enough memory for name\n");
		fflush(stderr);
		exit(EXIT_FAILURE);
	}
	if (indexOfMember == -1) {
		return;
	}
	fprintf(stderr, "[DEBUG] ENSURING CREW MEMBER IS VALID CAPTAIN\n");
	if (!highestRank(crew, indexOfMember)) {
		printf("Crew member is not a valid captain.\nCaptain must not have any crew members with a higher rank than them.\n");
		fflush(stdout);
		return;
	}
	sprintf(s, "Crewman%d", indexOfMember+1);
	if (!strcmp(crew[indexOfMember].name, s)) {
		printf("Crew member is not a valid captain.\nCaptain must have been assigned a unique name.\n");
		fflush(stdout);
		return;
	}
	fprintf(stderr, "[DEBUG] FREEING MEMORY\n");
	free(s);
	fprintf(stderr, "[DEBUG] SETTING CREW MEMBER AS CAPTAIN\n");
	fflush(stderr);
	crew[indexOfMember].post = CAPTAIN;
	for (int i = 0; i < NUMBER_OF_CREW_MEMBERS; i++) {
		crew[i].captain = &crew[indexOfMember];
		if (crew[i].post == CAPTAIN && i != indexOfMember) {
			crew[i].post = CREWMAN;
		}
	}
}

// Function to assign a post to a crew member
void assignPost(CrewMember crew[], int serviceNumber) {
    int indexOfMember = locateCrewMember(crew, serviceNumber);
	char *input = malloc(sizeof(char));
	char *s = calloc(INITIAL_NAME_LENGTH, sizeof(char));
	if (s == NULL) {
		fprintf(stderr, "[ERROR] Not enough memory for name\n");
		fflush(stderr);
		exit(EXIT_FAILURE);
	}
	if (input == NULL) {
		fprintf(stderr, "[ERROR] Not enough memory for input\n");
		fflush(stderr);
		exit(EXIT_FAILURE);
	}
	if (indexOfMember == -1) {
		return;
	}
	printf("What post should the crew member be given?\n1 - Communications Officer\n2 - Navigation Officer\n3 - Engineering Officer\n4 - Security Officer\n");
	fflush(stdout);
	scanf("%s", input);
	switch(*input) {
		case '1':
		fprintf(stderr, "[DEBUG] ENSURING CREW MEMBER IS VALID BRIDGE CREW\n");
		sprintf(s, "Crewman%d", indexOfMember+1);
		if (!strcmp(crew[indexOfMember].name, s)) {
			printf("Crew member is not a valid bridge crew member.\nBridge crew must have been assigned a unique name.\n");
			fflush(stdout);
			return;
		}
		fprintf(stderr, "[DEBUG] FREEING MEMORY\n");
		free(s);
		crew[indexOfMember].post = COMMUNICATIONS_OFFICER;
		for (int i = 0; i < NUMBER_OF_CREW_MEMBERS; i++) {
			if (crew[i].post == COMMUNICATIONS_OFFICER && i != indexOfMember) {
				crew[i].post = CREWMAN;
			}
		}
		break;
		case '2':
		fprintf(stderr, "[DEBUG] ENSURING CREW MEMBER IS VALID BRIDGE CREW\n");
		sprintf(s, "Crewman%d", indexOfMember+1);
		if (!strcmp(crew[indexOfMember].name, s)) {
			printf("Crew member is not a valid bridge crew member.\nBridge crew must have been assigned a unique name.\n");
			fflush(stdout);
			return;
		}
		fprintf(stderr, "[DEBUG] FREEING MEMORY\n");
		free(s);
		crew[indexOfMember].post = NAVIGATION_OFFICER;
		for (int i = 0; i < NUMBER_OF_CREW_MEMBERS; i++) {
			if (crew[i].post == NAVIGATION_OFFICER && i != indexOfMember) {
				crew[i].post = CREWMAN;
			}
		}
		break;
		case '3':
		fprintf(stderr, "[DEBUG] ENSURING CREW MEMBER IS VALID BRIDGE CREW\n");
		sprintf(s, "Crewman%d", indexOfMember+1);
		if (!strcmp(crew[indexOfMember].name, s)) {
			printf("Crew member is not a valid bridge crew member.\nBridge crew must have been assigned a unique name.\n");
			fflush(stdout);
			return;
		}
		fprintf(stderr, "[DEBUG] FREEING MEMORY\n");
		free(s);
		crew[indexOfMember].post = ENGINEERING_OFFICER;
		for (int i = 0; i < NUMBER_OF_CREW_MEMBERS; i++) {
			if (crew[i].post == ENGINEERING_OFFICER && i != indexOfMember) {
				crew[i].post = CREWMAN;
			}
		}
		break;
		case '4':
		fprintf(stderr, "[DEBUG] ENSURING CREW MEMBER IS VALID BRIDGE CREW\n");
		sprintf(s, "Crewman%d", indexOfMember+1);
		if (!strcmp(crew[indexOfMember].name, s)) {
			printf("Crew member is not a valid bridge crew member.\nBridge crew must have been assigned a unique name.\n");
			fflush(stdout);
			return;
		}
		fprintf(stderr, "[DEBUG] FREEING MEMORY\n");
		free(s);
		crew[indexOfMember].post = SECURITY_OFFICER;
		for (int i = 0; i < NUMBER_OF_CREW_MEMBERS; i++) {
			if (crew[i].post == SECURITY_OFFICER && i != indexOfMember) {
				crew[i].post = CREWMAN;
			}
		}
		break;
	}
}

// Function to display the data of the crew members
void displayData(CrewMember crew[], FILE * output) {
	fprintf(output,
	"Captain : %s\n"
	"                        Skills           Abilities    \n"
	"Ser#   Post Rank  | N  E  T  L  D  | C  I  S  P  A  | Race Name\n", ((CrewMember*) crew[0].captain != NULL ? (((CrewMember*) crew[0].captain)->name) : "NONE"));
	for (int i = 0; i < NUMBER_OF_CREW_MEMBERS; i++) { //Print the captain first
		if (crew[i].post == CAPTAIN) {
			fprintf(output, LINE_OF_OUTPUT, crew[i].serviceNumber, getPost(crew[i]), getRank(crew[i]), crew[i].skills.navigation,
			crew[i].skills.engineering, crew[i].skills.tactics, crew[i].skills.leadership, crew[i].skills.diplomacy,
			crew[i].abilities.charisma, crew[i].abilities.intelligence, crew[i].abilities.strength, crew[i].abilities.psionics, crew[i].abilities.agility,
			getRace(crew[i]), crew[i].name);
		}
	}
	for (int i = 0; i < NUMBER_OF_CREW_MEMBERS; i++) {
		if (crew[i].post > CREWMAN && crew[i].post != CAPTAIN) { //Print all bridge crew that aren't the captain
			fprintf(output, LINE_OF_OUTPUT, crew[i].serviceNumber, getPost(crew[i]), getRank(crew[i]), crew[i].skills.navigation,
			crew[i].skills.engineering, crew[i].skills.tactics, crew[i].skills.leadership, crew[i].skills.diplomacy,
			crew[i].abilities.charisma, crew[i].abilities.intelligence, crew[i].abilities.strength, crew[i].abilities.psionics, crew[i].abilities.agility,
			getRace(crew[i]), crew[i].name);
		}
	}
	for (int i = 0; i < NUMBER_OF_CREW_MEMBERS; i++) {
		if (crew[i].post == CREWMAN) { //Print the non-bridge crew
			fprintf(output, LINE_OF_OUTPUT, crew[i].serviceNumber, getPost(crew[i]), getRank(crew[i]), crew[i].skills.navigation,
			crew[i].skills.engineering, crew[i].skills.tactics, crew[i].skills.leadership, crew[i].skills.diplomacy,
			crew[i].abilities.charisma, crew[i].abilities.intelligence, crew[i].abilities.strength, crew[i].abilities.psionics, crew[i].abilities.agility,
			getRace(crew[i]), crew[i].name);
		}
	}
	fflush(stdout);
}

// Function to load data from a file
void loadData(CrewMember crew[]) {
    FILE *file = fopen(DATA_FILE, "r");
    if (file == NULL) {
        fprintf(stderr, "[ERROR] Could not open file for reading\n");
        fflush(stderr);
    } else {
        char buffer[1024];
        for (int i = 0; i < NUMBER_OF_LINES_OF_HEADER; i++) {
            fgets(buffer, sizeof(buffer), file);
        }
        for (int i = 0; i < NUMBER_OF_CREW_MEMBERS; i++) {
            fscanf(file, LINE_OF_INPUT, &crew[i].serviceNumber, crew[i].name, buffer, &crew[i].abilities.charisma, &crew[i].abilities.intelligence, &crew[i].abilities.strength, &crew[i].abilities.psionics, &crew[i].abilities.agility, &crew[i].skills.navigation, &crew[i].skills.engineering, &crew[i].skills.tactics, &crew[i].skills.leadership, &crew[i].skills.diplomacy, buffer, buffer);
            parseRace(&crew[i], buffer);
            parseRank(&crew[i], buffer);
            parsePost(&crew[i], buffer);
        }
        fclose(file);
    }
}

// Function to find a crew member's index from the service number
int locateCrewMember(CrewMember crew[], int serviceNumber) {
    for (int i = 0; i < NUMBER_OF_CREW_MEMBERS; i++) {
        if (crew[i].serviceNumber == serviceNumber) {
            return i;
        }
    }
    return -1;
}

// Function to find the highest rank in the crew
int highestRank(CrewMember crew[], int length) {
    int max = crew[0].rank;
    for (int i = 1; i < length; i++) {
        if (crew[i].rank > max) {
            max = crew[i].rank;
        }
    }
    return max;
}

// Function to parse the post of a crew member
void parsePost(CrewMember *crewMember, char *post) {
    if (!strcmp("Crew", post)) {
		crewMember->post = CREWMAN;
		return;
	}
	if (!strcmp("Nav", post)) {
		crewMember->post = NAVIGATION_OFFICER;
		return;
	}
	if (!strcmp("Com", post)) {
		crewMember->post = COMMUNICATIONS_OFFICER;
		return;
	}
	if (!strcmp("Sec", post)) {
		crewMember->post = SECURITY_OFFICER;
		return;
	}
	if (!strcmp("Cap", post)) {
		crewMember->post = CAPTAIN;
		return;
	}
	if (!strcmp("Eng", post)) {
		crewMember->post = ENGINEERING_OFFICER;
		return;
	}
	fprintf(stderr, "[ERROR] Crewmember has invalid post\n");
	fflush(stderr);
	exit(EXIT_FAILURE);
}

// Function to parse the rank of a crew member
void parseRank(CrewMember *crewMember, char *rank) {
    if (!strcmp("Comm", rank)) {
		crewMember->rank = COMMANDER;
		return;
	}
	if (!strcmp("Lt", rank)) {
		crewMember->rank = LIEUTENANT;
		return;
	}
	if (!strcmp("Ens", rank)) {
		crewMember->rank = ENSIGN;
		return;
	}
	if (!strcmp("Spcr", rank)) {
		crewMember->rank = SPACER;
		return;
	}
	fprintf(stderr, "[ERROR] Crewmember has invalid rank\n");
	fflush(stderr);
	exit(EXIT_FAILURE);
}

// Function to parse the race of a crew member
void parseRace(CrewMember *crewMember, char *race) {
    if (!strcmp("T", race)) {
		crewMember->race = TERRAN;
		return;
	}
	if (!strcmp("V", race)) {
		crewMember->race = VENUSIAN;
		return;
	}
	if (!strcmp("M", race)) {
		crewMember->race = MARTIAN;
		return;
	}
	if (!strcmp("S", race)) {
		crewMember->race = SATURIAN;
		return;
	}

	fprintf(stderr, "[ERROR] Crewmember has invalid race\n");
	fflush(stderr);
	exit(EXIT_FAILURE);
}

// Function to get the race of a crew member as a string
char * getRace(CrewMember crewMember) {
    switch (crewMember.race) {
		case 0:
		return "T";
		case 1:
		return "M";
		case 2:
		return "V";
		case 3:
		return "S";
		default:
		fprintf(stderr, "[ERROR] Crewmember has invalid race\n");
		fflush(stderr);
		exit(EXIT_FAILURE);
	}   
}

// Function to get the rank of a crew member as a string
char * getRank(CrewMember crewMember) {
    switch(crewMember.rank) {
		case 0:
		return "Spcr";
		case 1:
		return "Ens";
		case 2:
		return "Lt";
		case 3:
		return "Comm";
		default:
		fprintf(stderr, "[ERROR] Crewmember has invalid rank\n");
		fflush(stderr);
		exit(EXIT_FAILURE);	
	}
}

// Function to get the post of a crew member as a string
char * getPost(CrewMember crewMember) {
    switch(crewMember.post) {
		case 0:
		return "Crew";
		case 1:
		return "Com";
		case 2:
		return "Nav";
		case 3:
		return "Eng";
		case 4:
		return "Sec";
		case 5:
		return "Cap";
		default:
		fprintf(stderr, "[ERROR] Crewmember has invalid post\n");
		fflush(stderr);
		exit(EXIT_FAILURE);
	}
}

// Function to ensure crew members generated are actually unique
int isUnique(CrewMember crew[], int i) {
	for (int j = 0; j < i; j++) {
		if (crew[j].serviceNumber == crew[i].serviceNumber) {
			return 0;
		}
	}
	return 1;
}
