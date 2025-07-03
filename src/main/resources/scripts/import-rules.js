#!/usr/bin/env node

const rulesResponse = await fetch('http://localhost:9998/rules');

const rulesJson = await rulesResponse.json();

const rules = rulesJson.content;

for (const rule of rules) {
    const ruleResponse = await fetch(`http://localhost:9998/rules/${rule.id}`);

    const ruleJson = await ruleResponse.json();

    const vendor = (rule.id.includes('OPT')) ? 'kiuwan' : 'contrast';

    const request = {
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

    const ruleVectorResponse = await fetch(`http://localhost:14080/ollama/rules`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(request)
    });

    if (ruleVectorResponse.status == 202) {
        console.log(`Rule ${ruleJson.id} saved successfully`);
    }
    else {
        console.error(`Error saving rule ${ruleJson.id}, response code was ${ruleVectorResponse.status}`);
    }
}
