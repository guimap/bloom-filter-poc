import { faker } from '@faker-js/faker'
import { createWriteStream } from 'fs'

function generateEmail() {
    return `${faker.string.uuid()}@example.com`
}

function generateUser(i) {
    return {      
        id: faker.string.uuid(),
        email: generateEmail(),
        name: faker.person.firstName(),
        number: i
    }
}

const file = createWriteStream('../users.csv', { encoding: 'utf8' })
file.write('id,email,name,number\n')

const TOTAL = 10_000_000

function writeUsers() {
    let i = 0
    function write() {
        let ok = true
        while (i < TOTAL && ok) {
            const user = generateUser(i)
            const line = `${user.id},${user.email},${user.name},${user.number}\n`
            ok = file.write(line)
            i++
        }
        if (i < TOTAL) {
            file.once('drain', write)
        } else {
            file.end()
             }
    }
    write()
}

writeUsers()