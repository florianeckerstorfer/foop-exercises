note
	description: "Retiree class, inherts from person"

class
	PERSON_RETIREE

inherit
	PERSON

create
	make

feature -- setter
	set_retiree_number (new_number: STRING)
		do
			retiree_number := new_number
		end

feature -- access
	retiree_number: STRING
		-- Pensionisten Nummer

end

