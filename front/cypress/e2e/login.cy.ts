describe('Login spec', () => {
    it('Login and logout successfully', () => {
        cy.intercept('POST', '/api/auth/login', {
            body: {
                id: 1,
                username: 'username',
                firstName: 'firstName',
                lastName: 'lastName',
                admin: true
            },
        })

        cy.intercept(
            {
                method: 'GET',
                url: '/api/session',
            },
            [])

        cy.visit('/login')
        cy.loginUser("yoga@studio.com", "test!1234")

        cy.get('.mat-raised-button').click()
        cy.url().should('include', '/sessions')

        cy.logoutUser()
        cy.url().should('include', '/')

        cy.wait(500)
        cy.get('[data-testId="loginBtn"]').should('exist')
    })

    it('Login fail when email and password are empty', () => {
        cy.intercept('POST', '/api/auth/login', {
            statusCode: 400,
            body: {
                email: "ne doit pas être vide",
                password: "ne doit pas être vide",
            },
        }).as('loginFailedResponse')

        cy.visit('/login')
        cy.loginUser()

        cy.wait('@loginFailedResponse').its('response').should((response) => {
            expect(response.statusCode).to.be.equal(400)
            expect(response.body).to.contain({
                email: "ne doit pas être vide",
                password: "ne doit pas être vide",
            })
        })

        cy.get('[data-testId="errorContainer"]').as('errorContainer')
        cy.get('@errorContainer')
            .should('exist')
            .and('be.visible')
            .and('contain', 'An error occurred')


    })
});