note
	description: "Summary description for {ACCOUNT_RETIREE}."

class
	ACCOUNT_RETIREE

inherit
	ACCOUNT
		rename
			make as make_account
		redefine
			add_signer,
			account_type,
			min_sum,
			min_debit_interest,
			max_debit_interest,
			min_credit_interest,
			max_credit_interest,
			min_credit_limit
		end

create
	make

feature {NONE} -- initialize
	make (retiree: PERSON_RETIREE new_amount: DOUBLE c_limit: DOUBLE d_interest: DOUBLE c_interest: DOUBLE)
		do
			make_account(retiree, new_amount, c_limit, d_interest, c_interest)
		end

feature -- setter
	add_signer (signer: PERSON_RETIREE)
		do
			if signers = Void then
				create signers.make
			end
			signers.extend (signer)
		ensure then
			only_one_signer: signers.count.abs = 1
		end

feature -- constants
	account_type: STRING
		once
			Result := "RETIREE ACCOUNT"
		end

	min_sum: DOUBLE
		once
			Result := 1
		end

	min_debit_interest: DOUBLE
		once
			Result := 0.01
		end
	max_debit_interest: DOUBLE
		once
			Result := 0.5
		end

	min_credit_interest: DOUBLE
		once
			Result := 0.1
		end

	max_credit_interest: DOUBLE
		once
			Result := 0.5
		end

	min_credit_limit: DOUBLE
		once
			Result := -10
		end

end
