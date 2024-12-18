describe('Me spec', () => {


    it('Sessions list create and detail buttons are displayed properly', () => {
        cy.intercept('GET', '/api/session', {
            body: [
                {
                    id: 1,
                    name: "session 1",
                    date: "2024-12-07T00:00:00.000+00:00",
                    teacher_id: 1,
                    description: "session 1 description",
                    createdAt: "2024-12-07T13:57:10",
                    updatedAt: "2024-12-07T13:57:10"
                }
            ],
        }).as('getAllSessionsReq')

        cy.visit('/login')
        cy.loginUser("yoga@studio.com", "test!1234")
        cy.get('.mat-raised-button').click()

        cy.url().should('include', '/sessions')

        cy.wait('@getAllSessionsReq').its('response').should((response) => {
            expect(response.statusCode).to.be.equal(200)
            expect(response.body.length).to.equal(1)
            const sessionData = response.body[0]
            expect(sessionData).to.contain(
                {
                    id: 1,
                    name: "session 1",
                    date: "2024-12-07T00:00:00.000+00:00",
                    teacher_id: 1,
                    description: "session 1 description",
                    createdAt: "2024-12-07T13:57:10",
                    updatedAt: "2024-12-07T13:57:10"
                }
            )
        })

        cy.get('[fxlayout="row"] > .mat-card-header-text > .mat-card-title').should('have.text', 'Rentals available')
        cy.get('.items > .mat-card > .mat-card-header > .mat-card-header-text > .mat-card-title').should('have.text', 'session 1')
        cy.get('[data-testId="sessionDescription"]').invoke('text').should('match', /session 1 description/)

        cy.get('[data-testid="createBtn"]').should('exist')
        cy.get('[data-testid="detailBtn"]').should('exist')
        cy.logoutUser()
        cy.url().should('include', '/')

        cy.wait(500)
        cy.get('[data-testId="loginBtn"]').should('exist')
    })

    it('Session details and delete button are displayed properly', () => {
        cy.intercept('GET', '/api/session/**', {
            body: {
                id: 1,
                name: "session 1",
                date: "2024-12-07T00:00:00.000+00:00",
                teacher_id: 1,
                description: "session 1 description",
                createdAt: "2024-12-07T13:57:10",
                updatedAt: "2024-12-07T13:57:10"
            }
            ,
        }).as('createSessionReq')

        cy.visit('/login')
        cy.loginUser("yoga@studio.com", "test!1234")
        cy.get('.mat-raised-button').click()

        cy.url().should('include', '/sessions')

        cy.get('[data-testid="detailBtn"]').should('exist').click()
        cy.url().should('contain', '/sessions/detail/')

        cy.wait('@createSessionReq').its('response').should((response) => {
            expect(response.statusCode).to.be.equal(200)
            const sessionData = response.body
            expect(sessionData).to.contain(
                {
                    id: 1,
                    name: "session 1",
                    date: "2024-12-07T00:00:00.000+00:00",
                    teacher_id: 1,
                    description: "session 1 description",
                    createdAt: "2024-12-07T13:57:10",
                    updatedAt: "2024-12-07T13:57:10"
                }
            )
        })
        cy.get('[data-testid="deleteBtn"]').should('exist')

        cy.get('h1').should('have.text', 'Session 1')

        cy.get('[data-testid="deleteBtn"]').should('exist')

        cy.logoutUser()
        cy.url().should('include', '/')

        cy.wait(500)
        cy.get('[data-testId="loginBtn"]').should('exist')
    })

    it('Session creation works properly', () => {
        cy.intercept('POST', '/api/session', {
            body: {
                "id": 2,
                "name": "session 2",
                "date": "2012-01-01T00:00:00.000+00:00",
                "teacher_id": 1,
                "description": "my description",
                "users": null,
                "createdAt": "2024-12-18T15:28:20.074",
                "updatedAt": "2024-12-18T15:28:20.111"
            },
        }).as('createSessionReq')

        cy.visit('/login')
        cy.loginUser("yoga@studio.com", "test!1234")
        cy.get('.mat-raised-button').click()

        cy.url().should('include', '/sessions')

        cy.get('[data-testid="createBtn"]').should('exist').click()
        cy.url().should('contain', '/sessions/create')

        cy.get('#mat-input-2').type('session test{enter}')
        cy.get('#mat-input-3').type('2024-12-12')
        cy.get('mat-select[formControlName="teacher_id"]').click().get('mat-option').contains('Margot DELAHAYE').click();
        cy.get('#mat-input-4').type('session description')


        cy.get('button[type="submit"]').should('exist').click()

        cy.wait('@createSessionReq').its('response').should((response) => {
            expect(response.statusCode).to.be.equal(200)
            const sessionData = response.body
            expect(sessionData).to.contain(
                {
                    id: 2,
                    name: "session 2",
                    date: "2012-01-01T00:00:00.000+00:00",
                    teacher_id: 1,
                    description: "my description",
                    users: null,
                    createdAt: "2024-12-18T15:28:20.074",
                    updatedAt: "2024-12-18T15:28:20.111"
                }
            )
        })
        cy.logoutUser()
        cy.url().should('include', '/')

        cy.wait(500)
        cy.get('[data-testId="loginBtn"]').should('exist')
    })

    it('Session creation works properly', () => {
        cy.intercept('POST', '/api/session', {
            body: {
                "id": 2,
                "name": "session 1",
                "date": "2012-01-01T00:00:00.000+00:00",
                "teacher_id": 1,
                "description": "my description",
                "users": null,
                "createdAt": "2024-12-18T15:28:20.074",
                "updatedAt": "2024-12-18T15:28:20.111"
            },
        }).as('updateSessionReq')

        cy.visit('/login')
        cy.loginUser("yoga@studio.com", "test!1234")
        cy.get('.mat-raised-button').click()

        cy.url().should('include', '/sessions')

        cy.get('[data-testid="createBtn"]').should('exist').click()
        cy.url().should('contain', '/sessions/create')

        cy.get('#mat-input-2').type('session test{enter}')
        cy.get('#mat-input-3').type('2024-12-12')
        cy.get('mat-select[formControlName="teacher_id"]').click().get('mat-option').contains('Margot DELAHAYE').click();
        cy.get('#mat-input-4').type('session description')


        cy.get('button[type="submit"]').should('exist').click()

        cy.wait('@updateSessionReq').its('response').should((response) => {
            expect(response.statusCode).to.be.equal(200)
            const sessionData = response.body
            expect(sessionData).to.contain(
                {
                    id: 2,
                    name: "session 1",
                    date: "2012-01-01T00:00:00.000+00:00",
                    teacher_id: 1,
                    description: "my description",
                    users: null,
                    createdAt: "2024-12-18T15:28:20.074",
                    updatedAt: "2024-12-18T15:28:20.111"
                }
            )
        })
        cy.logoutUser()
        cy.url().should('include', '/')

        cy.wait(500)
        cy.get('[data-testId="loginBtn"]').should('exist')
    })

    it('Session update works properly', () => {
        cy.intercept('PUT', '/api/session/**', {
            body: {
                "id": 2,
                "name": "session 1 updated",
                "date": "2012-01-01T00:00:00.000+00:00",
                "teacher_id": 1,
                "description": "my description updated",
                "users": null,
                "createdAt": "2024-12-18T15:28:20.074",
                "updatedAt": "2024-12-18T15:28:20.111"
            },
        }).as('updateSessionReq')

        cy.visit('/login')
        cy.loginUser("yoga@studio.com", "test!1234")
        cy.get('.mat-raised-button').click()

        cy.url().should('include', '/sessions')

        cy.get('[data-testid="updateBtn"]').should('exist').click()
        cy.url().should('contain', '/sessions/update/')

        cy.get('#mat-input-2').focus().clear().type('session 1 updated')
        cy.get('textarea[formControlName="description"]').focus().clear().type('session description updated')


        cy.get('button[type="submit"]').should('exist').click()
        cy.wait('@updateSessionReq').its('response').should((response) => {
            expect(response.statusCode).to.be.equal(200)
            const sessionData = response.body
            expect(sessionData).to.contain(
                {
                    id: 2,
                    name: "session 1 updated",
                    date: "2012-01-01T00:00:00.000+00:00",
                    teacher_id: 1,
                    description: "my description updated",
                    users: null,
                    createdAt: "2024-12-18T15:28:20.074",
                    updatedAt: "2024-12-18T15:28:20.111"
                }
            )
        })

        cy.logoutUser()
        cy.url().should('include', '/')

        cy.wait(500)
        cy.get('[data-testId="loginBtn"]').should('exist')
    })

    it('Session update works properly', () => {
        cy.intercept('DELETE', '/api/session/**', { statusCode: 200 }).as('deleteSessionReq')

        cy.visit('/login')
        cy.loginUser("yoga@studio.com", "test!1234")
        cy.get('.mat-raised-button').click()

        cy.url().should('include', '/sessions')

        cy.get('[data-testid="detailBtn"]').should('exist').click()
        cy.url().should('contain', '/sessions/detail/')


        cy.get('[data-testid="deleteBtn"]').should('exist').click()
        cy.wait('@deleteSessionReq').its('response').should((response) => {
            expect(response.statusCode).to.be.equal(200)

        })

        cy.logoutUser()
        cy.url().should('include', '/')

        cy.wait(500)
        cy.get('[data-testId="loginBtn"]').should('exist')
    })
});