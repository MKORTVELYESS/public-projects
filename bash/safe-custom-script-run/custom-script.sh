#!/bin/bash
#abort on failure
set -e
set -u

#Environment dependent input files
allowlist_file=$1
execute_file=$2

#Expect backout filename from execute filename - switch from-version with to-version
execute_filename="${execute_file##*/}"
IFS='_' read -ra version <<< "${execute_filename%.txt}"
backout_file="./${version[1]}_${version[0]}.txt"


# Function to check if a file exists
check_file_exists() {
    local file_path="$1"
    local is_required=$2
    
    if [ -e "$file_path" ]; then
        echo "File exists at $file_path."
        return 0
    else
  	echo "File does not exist at $file_path."
  	
    	if [ $is_required -eq 1 ]; then
    	    echo "Nothing to execute."
    	    exit 0
 	else
            echo "Can not proceed without $file_path."
            exit 1
        fi

    fi
}

# Validation to prevent OS command injection
is_cmd_whitelisted() {
    local cmd="$1"
    local allow_list="$2"
    
    #Shell special characters like ; || && > should be disallowed for security
    if [[ ! "$cmd" =~ ^[a-zA-Z0-9_\ \/\.-]+$ ]] ; then
    	echo "Special characters are not allowed in $cmd"
    	exit 1
    fi
    
    #Allow whitelisted commands only (flexible validation with regex)
    while IFS= read -r regex; do
    	#empty line in regex file should validate nothing (skip)
        if [[ -z "$regex" ]]; then 
            continue 
        fi
    		
        if [[ "$cmd" =~ $regex ]] ; then
            return 0
        fi
    done < "$allow_list"
    
    #If cmd did not match any regex --> not allowed
    echo "$cmd not allowed"
    return 1
}

#Collect cmds into an array - only proceed if all of them passed validation
register_commands() {

    local execute_file="$1"
    local allowlist_file="$2"
    local -n result=$3
    
    # Read each command from the execution file
    while IFS= read -r command; do
    
	#skip empty commands - should not be registered
	if [[ -z "$command" ]] ; then
	    continue
	fi
    
        is_cmd_whitelisted "$command" "$allowlist_file"

        echo "$command will be registered"
        result+=("$command")
    
     done < "$execute_file"
}

#---------------------------------------------------
echo "whoami $(eval "whoami")"

echo "Ensure required inputs exists"
echo "Execute must exist - if not exist exit with 0"
check_file_exists "$execute_file" 1
echo "Allow-list must exist"
check_file_exists "$allowlist_file" 0
echo "Backout must exist"
check_file_exists "$backout_file" 0

echo "Start validate EXECUTE"
declare -a commands_to_run
register_commands "$execute_file" "$allowlist_file" commands_to_run
echo "End validate EXECUTE"

#Validation only - to prepare for backout case
echo "Start validate BACKOUT"
declare -a backout_commands
register_commands "$backout_file" "$allowlist_file" backout_commands
echo "End validate BACKOUT"

echo "Start execution - will abort if non-zero exit code"
index=1
for cmd in "${commands_to_run[@]}"; do
   header="00${index}"
    echo "--------------${header: -3}--------------"
    echo "$cmd"
    (eval "$cmd") #eval in subshell to prevent env var changes
    ((index++))
done
