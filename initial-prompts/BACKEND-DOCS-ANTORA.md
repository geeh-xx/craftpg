# Antora Documentation

Location:
- back-end/docs/documentation/

Files:
- antora-playbook.yml
- antora.yml
- modules/ROOT/nav.adoc
- modules/ROOT/pages/*.adoc

Build:
- from back-end:
  make docs

Output:
- back-end/docs/documentation/build/site