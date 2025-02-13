```Initialize program:
    Generate initial crew members
    Print program instructions

Loop until end of input:
    Prompt user for input
    Respond to user input

End of loop

Handle user requests:
    If user requested to display information:
        Display crew information

    If user requested to rename a crew member:
        Find crew member with given ID
        Change name of crew member

    If user requested to promote a crew member to captain:
        Find crew member with given ID
        Ensure they are eligible for captaincy
        Promote them to captain
        Demote previous captain to crew member

    If user requested to assign a crew member to bridge crew:
        Find crew member with given ID
        Ensure they are eligible for bridge crew
        Prompt for new post
        Assign them to new post
        Reassign previous post owner to new post crew member

    If user requested to regenerate a crew member:
        Regenerate crew member without changing name, ID, or post

    If user requested to save crew:
        Display crew information and output to file 'crewmember_list.txt'

    If user requested to load crew:
        Load crew from file 'crewmember_list.txt'

    If user is finished:
        Free memory and exit program
