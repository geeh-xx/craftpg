---
description: 'Generate unit tests for the current file'
agent: 'agent'
tools: ['search', 'read', 'edit']
---
Generate unit tests for [${fileBasename}](${file}).

* Place the test file in the same directory: ${fileDirname} on test folder "src/test/java/**/*.java"
* Name the test file: ${fileBasenameNoExtension}Test.java
* Follow testing conventions in: [test.instruction.md](../instructions/test.instructions.md)

If there is a selection, only generate tests for this code:
${selection}
