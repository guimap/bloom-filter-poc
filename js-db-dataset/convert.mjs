// crie um script que le um arquivo csv e converte para um script SQL que sera salvo no caminho "../init/script.sql"", deve considerar o conteudo ja existente no arquivo"
import { readFileSync, writeFileSync } from 'fs';
import path from 'path';
import { fileURLToPath } from 'url';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);
const csvFilePath = path.join(__dirname, './users.csv');
const sqlFilePath = path.join(__dirname, '../init/script.sql');
const csvContent = readFileSync(csvFilePath, 'utf8');
const sqlContentPrevious = readFileSync(sqlFilePath, 'utf8');
const lines = csvContent.split('\n');
const header = lines[0].split(',');
const tableName = 'users';
const sqlLines = lines.slice(1).map(line => {
    const values = line.split(',').map(value => `"${value}"`).join(',');
    return `INSERT INTO ${tableName} (${header.join(',')}) VALUES (${values});`;
});
const sqlContent = `
USE myDB;
CREATE TABLE IF NOT EXISTS users (
  id VARCHAR(255) PRIMARY KEY,
  name VARCHAR(255),
  email VARCHAR(255)
);
\n${sqlLines.join('\n')}\n-- IGNORE --\n`;
writeFileSync(sqlFilePath, sqlContent, 'utf8');
// Exibe uma mensagem de sucesso
console.log(`SQL script has been saved to ${sqlFilePath}`);