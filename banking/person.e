note
	description: "Summary description for {PERSON}."
	author: ""
	date: "$Date$"
	revision: "$Revision$"

class
	PERSON

create
	make

feature
	name: STRING

feature {NONE}
	make(person_name: STRING)
	do
		name := person_name
	end

end
