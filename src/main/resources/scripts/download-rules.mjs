#!/usr/bin/env node
import path from 'path'
import fs from 'fs';
import yazl from 'yazl';

const writeZipFile = (zipfile, name) => {

  const outputPath = path.join('..', 'rules', `${name}.zip`);

  zipfile.outputStream.pipe(fs.createWriteStream(outputPath)).on("close", () => {
    console.log(`${name} rules written to ${outputPath}`);
  });

  zipfile.end();
};

const rulesResponse = await fetch('http://localhost:9998/rules');

const rulesJson = await rulesResponse.json();

const rules = rulesJson.content;

const contrastRulesZip = new yazl.ZipFile();
const kiuwanRulesZip = new yazl.ZipFile();

for (const rule of rules) {
  const ruleResponse = await fetch(`http://localhost:9998/rules/${rule.id}`);

  const ruleJson = await ruleResponse.json();

  const vendor = (rule.id.includes('OPT')) ? 'kiuwan' : 'contrast';

  const outputJson = {
    id: ruleJson.id,
    name: ruleJson.name,
    severity: ruleJson.severity,
    category: ruleJson.category,
    language: ruleJson.language || [],
    cwe: ruleJson.cwe || [],
    description: ruleJson.description,
    risk: ruleJson.risk,
    issue: ruleJson.issue,
    advice: ruleJson.advice,
    vendor
  };

  const targetZip = ('contrast' === vendor)
    ? contrastRulesZip
    : kiuwanRulesZip;

  targetZip.addBuffer(
    Buffer.from(JSON.stringify(outputJson, null, 2)),
    `${rule.id}.json`
  );

  console.info(`Downloaded ${rule.id}`);
}

writeZipFile(contrastRulesZip, 'contrast');
writeZipFile(kiuwanRulesZip, 'kiuwan');

