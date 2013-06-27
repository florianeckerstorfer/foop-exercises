note
	description: "Normal bank account"

class
	ACCOUNT

inherit
	ANY
		redefine
			out
		end

create
	make

feature -- initialize
	make (signer: PERSON new_amount: DOUBLE c_limit: DOUBLE d_interest: DOUBLE c_interest: DOUBLE)
		-- constructor
		require
			min_amount: amount >= 0
		do
			amount := new_amount
			set_credit_limit(c_limit)
			set_debit_interest(d_interest)
			set_credit_interest(c_interest)

			set_owner (signer)
			add_signer (signer)
		end

feature -- element change
	deposit (sum: DOUBLE)
		-- deposit money
		require
			transaction_ok: sum >= min_sum
		do
			amount := amount + sum
		end

	disburse (sum: DOUBLE)
		-- disburse money
		require
			transaction_ok: sum >= min_sum
		do
			amount := amount - sum
		ensure
			amount_ok: amount >= credit_limit
		end

	transfer (payee: ACCOUNT sum: DOUBLE)
		-- transfer money to different account
		require
			payee_ok: payee /= Void
			transaction_ok: sum >= min_sum
		do
			disburse (sum)
			payee.deposit (sum)
		ensure
			amount_ok: amount >= credit_limit
		end

feature -- setter
	set_credit_limit (new_credit_limit: DOUBLE)
		do
			credit_limit := new_credit_limit
		end

	set_debit_interest (new_debit_interest: DOUBLE)
		do
			debit_interest := new_debit_interest
		end

	set_credit_interest (new_credit_interest: DOUBLE)
		do
			credit_interest := new_credit_interest
		end

	set_owner (new_owner: PERSON)
		do
			owner := new_owner
		end

	add_signer (signer: PERSON)
		do
			if signers = Void then
				create signers.make
			end
			signers.extend (signer)
		end

feature -- accesss
	amount: DOUBLE
		-- Betrag
	credit_limit: DOUBLE assign set_credit_limit
		-- Kreditrahmen
	debit_interest: DOUBLE assign set_debit_interest
		-- Habenverzinsung
	credit_interest: DOUBLE assign set_credit_interest
		-- Sollverzinsung		
	owner: PERSON
		-- Besitzer
	signers: LINKED_LIST[PERSON]
		-- Zeichnungsberechtigte, erster Zeichnungberechtigter ist der Besitzer des Kontos

feature -- constants
	account_type: STRING
		once
			Result := "ACCOUNT"
		end

	min_sum: DOUBLE
		-- minimum amount of money of banking action
		once
			Result := 2
		end

	min_debit_interest: DOUBLE
		-- minimal debit interest
		once
			Result := 0.01
		end
	max_debit_interest: DOUBLE
		-- maximal debit interest
		once
			Result := 0.75
		end

	min_credit_interest: DOUBLE
		-- maximal credit interest
		once
			Result := 0.1
		end

	max_credit_interest: DOUBLE
		-- minimal credit interest
		once
			Result := 0.5
		end

	min_credit_limit: DOUBLE
		-- minimal credit limit
		once
			Result := -1000
		end

feature --output
	out: STRING
		-- return the whole information of the account in a printable string
		do
			Result := "%N" + account_type + "%NOwner: " + owner.name + "%NSigners: "

			across signers as signer
			loop
				Result := Result + signer.item.name
				if not signer.is_last then
					Result := Result + ", "
				end
			end

			Result := Result + "%NAmount: " + amount.out + "%NCredit limit: " + credit_limit.out + "%N"
			Result := Result + "Debit interest: " + debit_interest.out + "%NCredit interest: " + credit_interest.out + "%N"
		end

invariant
	credit_limit_ok: credit_limit >= min_credit_limit
	credit_interest_ok: credit_interest >= min_credit_interest and credit_interest <= max_credit_interest
	debit_interest_ok: debit_interest >= min_debit_interest and debit_interest <= max_debit_interest
	signer_ok: signers.first /= Void
end
