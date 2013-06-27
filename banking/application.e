note
	description : "Banking application"

class
	APPLICATION

inherit
	ARGUMENTS

create
	make

feature {NONE} -- Initialization

	make
			-- Run application.
		local
			person: PERSON
			student: PERSON_STUDENT
			student1: PERSON_STUDENT
			retiree: PERSON_RETIREE

			account: ACCOUNT
			account_student: ACCOUNT_STUDENT
			account_retiree: ACCOUNT_RETIREE

			account_cat_call: ACCOUNT -- ACCOUNT_RETIREE
		do
			print ("%N%NBANKING APPLICATION%N")

			-- create persons
			create person.make ("Hansjoerg Kammerer")
			create student.make ("Thomas Reiter")
			student.set_matriculation_number("0726471")
			create student1.make ("Herbert Reiter")
			student1.set_matriculation_number("0821231")
			create retiree.make ("Sabrina Leitner")
			retiree.set_retiree_number("198625")

			-- create normal account
			create account.make (person, 1000, -1000, 0.7, 0.2)
			account.add_signer (student)
			account.add_signer (retiree)
			account.deposit (2)

			print(account.out)

			-- create student account
			create account_student.make (student, 100, 0, 0.3, 0.3)
			account_student.deposit (1)
			account_student.disburse (2)

			print(account_student.out)

			-- create retiree accounts
			create account_retiree.make (retiree, 500, -10, 0.3, 0.5)
			account_retiree.deposit (1)
			account_retiree.disburse (2)

			print(account_retiree.out)

			-- transfer money between accountss
			print ("%NTransfer 100 from " + account.signers.first.name + " to " + account_student.signers.first.name + "%N")
			account.transfer (account_student, 100)
			print ("Transfer 10 from " + account_student.signers.first.name + " to " + account_retiree.signers.first.name + "%N")
			account_student.transfer (account_retiree, 10)
			print ("Transfer 20 from " + account_retiree.signers.first.name + " to " + account.signers.first.name + "%N")
			account_retiree.transfer (account, 20)

			print(account.out)
			print(account_student.out)
			print(account_retiree.out)

			-- create cat call
			-- http://dev.eiffel.com/Covariance_through_renaming
			print ("%N%N%NCAT CALL:%N=========%N")
			account_cat_call := account_retiree
			account_cat_call.set_owner (student)

			print(account_cat_call.out)
			--print("%NRetiree number (via account): " + account_cat_call.owner.retiree_number);
		end

end
