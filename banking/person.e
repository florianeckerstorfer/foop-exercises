note
	description: "Person class"

class
	PERSON

create
	make

feature
	name: STRING

feature -- initilaize
	make(person_name: STRING)
	do
		name := person_name
	end

end
