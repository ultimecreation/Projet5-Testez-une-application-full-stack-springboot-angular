describe('Register spec', () => {
    it('Register successfull', () => {
        cy.intercept('POST', '/api/auth/register', {
            body: {
                message: "User registered successfully!"
            },
        }).as('registerSuccessReq')

        cy.visit('/register')
        cy.get('#mat-input-0').type("firstname")
        cy.get('#mat-input-1').type("lastname")
        cy.get('#mat-input-2').type("mail@mail.com")
        cy.get('#mat-input-3').type("test!1234")

        cy.get('.mat-button-wrapper').click()
        cy.wait('@registerSuccessReq').its('response.statusCode').should('eq', 200)

        cy.url().should('contain', '/login')
    })

    it('Register fail when email is already taken', () => {
        cy.intercept('POST', '/api/auth/register', {
            statusCode: 400,
            body: {
                message: "Error: Email is already taken!"
            }
        }).as('registerFailed')

        cy.visit('/register')
        cy.get('#mat-input-0').type("firstname")
        cy.get('#mat-input-1').type("lastname")
        cy.get('#mat-input-2').type("mail@mail.com")
        cy.get('#mat-input-3').type("test!")
        cy.get('.mat-button-wrapper').click()

        cy.wait('@registerFailed').its('response').should((response) => {
            expect(response.statusCode).to.be.equal(400)
            expect(response.body).to.contain({
                message: "Error: Email is already taken!"
            })
        })

        cy.get('[data-testId="errorContainer"]').as('errorContainer')
        cy.get('@errorContainer')
            .should('exist')
            .and('be.visible')
            .and('contain', 'An error occurred')
    })
});