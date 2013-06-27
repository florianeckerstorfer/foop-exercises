note
	description: "Student account for students, inherts from account"

class
	ACCOUNT_STUDENT

inherit
	ACCOUNT
		rename
			make as make_account
		redefine	-- redefine/overwrite functions/constants/variables
			add_signer,
			account_type,
			min_sum,
			min_debit_interest,
			max_debit_interest,
			min_credit_interest,
			max_credit_interest,
			min_credit_limit,
			owner,
			set_owner
		end

create
	make

feature -- initialize
	make (student: PERSON_STUDENT new_amount: DOUBLE c_limit: DOUBLE d_interest: DOUBLE c_interest: DOUBLE)
		-- constructor calls constructor of inherited class
		do
			make_account(student, new_amount, c_limit, d_interest, c_interest)
		end

feature -- setter
	add_signer (signer: PERSON_STUDENT)
		do
			if signers = Void then
				create signers.make
			end
			signers.extend (signer)
		ensure then
			only_one_signer: signers.count.abs = 1
		end

	set_owner (new_owner: PERSON_STUDENT)
		do
			owner := new_owner
		end

feature -- access
	owner: PERSON_STUDENT
		-- Student

feature -- constants
	account_type: STRING
		once
			Result := "STUDENT ACCOUNT"
		end

	min_sum: DOUBLE
		-- minimum amount of money of banking action
		once
			Result := 1
		end

	min_debit_interest: DOUBLE
		once
			Result := 0.01
		end
	max_debit_interest: DOUBLE
		once
			Result := 0.4
		end

	min_credit_interest: DOUBLE
		once
			Result := 0.1
		end

	max_credit_interest: DOUBLE
		once
			Result := 0.3
		end

	min_credit_limit: DOUBLE
		once
			Result := 0
		end

end
