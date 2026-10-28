#!/bin/bash
for file in *; do
	if [ -f "$file" ]; then
		lower_file="${file,,}"
		if [ "$file" != "$lower_file" ]; then
			mv -n -- "$file" "$lower_file"
		fi
	fi
done
