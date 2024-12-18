describe('Me spec', () => {
    it('User infos are display properly', () => {
        cy.intercept('GET', '/api/user/**', {
            body: {
                id: 1,
                email: "yoga@studio.com",
                lastName: "Admin",
                firstName: "Admin",
                admin: true,
                createdAt: "2024-12-02T15:33:17",
                updatedAt: "2024-12-02T15:33:17"
            },
        }).as('accountInfoReq')

        cy.visit('/login')
        cy.loginUser("yoga@studio.com", "test!1234")
        cy.get('.mat-raised-button').click()
        cy.get('[data-testId="accountBtn"]').click()
        cy.url().should('include', '/me')

        cy.wait('@accountInfoReq').its('response').should((response) => {
            expect(response.statusCode).to.be.equal(200)
            expect(response.body).to.contain({
                id: 1,
                email: "yoga@studio.com",
                lastName: "Admin",
                firstName: "Admin",
                admin: true,
                createdAt: "2024-12-02T15:33:17",
                updatedAt: "2024-12-02T15:33:17"
            })
        })

        cy.get('h1').should('have.text', 'User information')
        cy.get('.mat-card-content > div.ng-star-inserted > :nth-child(1)').should('have.text', 'Name: Admin ADMIN')
        cy.get('.mat-card-content > div.ng-star-inserted > :nth-child(2)').should('have.text', 'Email: yoga@studio.com')
        cy.get('.mat-card-content > div.ng-star-inserted > :nth-child(3)').should('have.text', 'You are admin')

        cy.logoutUser()
        cy.url().should('include', '/')

        cy.wait(500)
        cy.get('[data-testId="loginBtn"]').should('exist')
    })
});