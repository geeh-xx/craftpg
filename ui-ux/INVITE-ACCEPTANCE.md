# Invite Acceptance Page

Route: /invites/[token]

## Objective
Allow invited user to join campaign.

---

## Layout

Section 1:
- Campaign name
- Roles assigned
- Expiration date

Section 2:
- Character selection list

Section 3:
- Accept button

---

## Character Selection

Options:
- Existing character (card list)
- Create new character
- Generate random character

---

## Rules

- Accept button disabled until character selected
- If not authenticated → prompt login
- If no characters exist → force create

---

## Components

- Card
- Button
- Alert
- Modal (character creation)