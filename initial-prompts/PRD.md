# PRD — CraftPG (Tormenta20 Campaign Manager V1)

## 1. Overview
CraftPG is a Tormenta20 campaign manager that structures content into campaigns and sessions, with collaborative roles and a character-based participation model. Users join campaigns by accepting invites and must select or create a character. Characters are locked per campaign and can have different progression states across different campaigns.

## 2. Goals
- Provide a clean workflow for DM-led campaign management
- Reduce friction for inviting players and onboarding them with a character
- Provide a visual, editable Tormenta20 character sheet
- Track sessions with notes, attendance, XP, and session assets

## 3. Non-goals (V1)
- Full Tormenta20 rules engine with 100% edge-case automation
- VTT features (grid, combat, fog of war, etc.)

## 4. Personas
### 4.1 DM (Campaign owner)
- Creates campaigns and sessions
- Invites members and assigns roles
- Edits campaign/session content
- Manages session XP and outcomes

### 4.2 Moderator (Assistant admin)
- Helps DM manage campaigns and sessions
- Cannot delete or finish campaigns

### 4.3 Player
- Accepts invite and joins with a character
- Views campaign/session data
- Edits their character (within allowed boundaries)

## 5. Key business rules
1) Only authenticated users can access the system.
2) Campaign creator becomes DM.
3) Roles: DM, MODERATOR, PLAYER (multi-role allowed).
4) Only DM/MODERATOR can edit campaign and session content.
5) Moderator cannot delete or finish a campaign.
6) Invites define roles in advance.
7) Accepting an invite requires selecting/creating a character.
8) No user can exist in a campaign without a character membership.
9) Character is locked per campaign (no switching).
10) Same character concept can be used in multiple campaigns with different progression.

## 6. User flows

### 6.1 Authentication
- User signs in via Keycloak
- Email verification and social login supported by Keycloak configuration

### 6.2 Create campaign
- DM enters title, description, frequency, status
- Campaign is created; DM role assigned to creator

### 6.3 Invite user to campaign
- DM/mod enters email and selects roles
- System creates invite (token + expiry)
- Email sent + in-app notification created for existing user

### 6.4 Accept invite (mandatory character)
- Invite link opens “Invite details”
- If not logged in → user signs in
- User selects:
  - existing character base OR
  - create new manual OR
  - generate random
- On confirm:
  - campaign character instance created
  - membership created
  - roles assigned
  - invite marked accepted

### 6.5 Sessions
- DM/mod creates sessions freely (no fixed number)
- Each session contains:
  - schedule date/time, summary, notes
  - attendance list
  - XP logs
  - NPCs, maps, treasures

### 6.6 Character sheet and level-up
- Visual sheet with sections (attributes, skills, powers, spells, inventory, notes)
- Edits persist to the campaign character instance
- “Level up” wizard enforces happy-path class/race rules
- XP increments can be session-driven or manual (V1 supports manual + session XP records)

## 7. Functional requirements

### Auth
- FR-A1: Authenticate via Keycloak OIDC
- FR-A2: Deny access to all app features without auth

### Campaigns
- FR-C1: Create campaign
- FR-C2: List campaigns (where user has any role)
- FR-C3: View campaign details
- FR-C4: Update campaign (DM/MODERATOR)
- FR-C5: Delete campaign (DM only)
- FR-C6: Finish campaign (DM only)

### Roles
- FR-R1: Assign DM role to creator
- FR-R2: Multi-role support (MODERATOR + PLAYER)
- FR-R3: Permission enforcement in API and UI

### Invites
- FR-I1: Create invite by email with role selection
- FR-I2: Invite token with expiry
- FR-I3: Public invite lookup (token)
- FR-I4: Accept invite requires auth + characterBaseId
- FR-I5: Apply roles + create membership + lock character

### Characters
- FR-CH1: Create character base manually
- FR-CH2: Generate random character base (basic V1)
- FR-CH3: Create per-campaign character instance on join
- FR-CH4: Visual sheet rendering
- FR-CH5: Edit sheet data over time
- FR-CH6: Level-up wizard (happy-path validation)

### Sessions
- FR-S1: Create session (DM/MODERATOR)
- FR-S2: Edit session (DM/MODERATOR)
- FR-S3: Attendance tracking
- FR-S4: XP logging per participant
- FR-S5: CRUD for NPCs, maps, treasures in a session

## 8. Non-functional requirements
- NFR-1: Secure authorization checks (server-side)
- NFR-2: Data isolation per campaign
- NFR-3: Mobile-friendly sheet UI
- NFR-4: Performance: pagination for lists, lazy loading heavy sections
- NFR-5: Audit fields (created_at/updated_at) for key tables

## 9. UI requirements (Flowbite components)
- Consistent light theme, clean layout
- Use Flowbite for:
  - Navigation (Navbar, Sidebar)
  - Layout (Cards)
  - Forms (Inputs, Select, Textarea, Checkbox, Modal)
  - Data display (Table, Badge, Tabs, Accordion)
  - Feedback (Toast, Alerts, Loading spinners)

## 10. Acceptance criteria (high value)
- AC-INV-01: Invite cannot be accepted without selecting a character
- AC-MEM-01: A user cannot have membership in a campaign without a campaign_character
- AC-CHAR-01: Character cannot be switched after joining (no endpoint / no UI)
- AC-PERM-01: Moderator cannot delete/finish campaign (API returns forbidden)
- AC-SES-01: Session records attendance + XP + NPC/map/treasure entries

## 11. Rollout plan (V1)
- Phase 1: Auth + campaigns + invites + join with character (MVP core)
- Phase 2: Sessions + session assets
- Phase 3: Sheet UX polish + level-up wizard