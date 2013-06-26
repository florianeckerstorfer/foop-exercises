note
	description: "Student class, inherts from person"

class
	PERSON_STUDENT

inherit
	PERSON

create
	make

feature -- setter
	set_matriculation_number (new_number: STRING)
		do
			matriculation_number := new_number
		end

feature -- access
	matriculation_number: STRING
		-- Matrikelnummer

end
