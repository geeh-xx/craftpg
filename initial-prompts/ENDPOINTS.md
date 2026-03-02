# API Endpoints (Summary)

Public:
- GET /health
- GET /invites/{token}

Protected (Bearer JWT):
Campaigns:
- GET /campaigns
- POST /campaigns
- GET /campaigns/{campaignId}
- PATCH /campaigns/{campaignId}           (DM/MODERATOR)
- DELETE /campaigns/{campaignId}          (DM only)
- POST /campaigns/{campaignId}/finish     (DM only)

Invites:
- POST /campaigns/{campaignId}/invites    (DM/MODERATOR)
- POST /invites/{token}/accept            (auth required + characterBaseId required)

Characters:
- GET /characters
- POST /characters
- POST /characters/generate-random

Sessions:
- POST /campaigns/{campaignId}/sessions   (DM/MODERATOR)

Authorization rules:
- DM: everything
- MODERATOR: everything except delete/finish campaign
- PLAYER: read campaign + edit own campaign character only (future endpoints)