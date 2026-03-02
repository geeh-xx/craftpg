# craftpg-web

Aplicação web do CraftPG.

## Tecnologias e frameworks
- TypeScript
- Next.js App Router
- React
- NextAuth.js com Keycloak
- Flowbite React + Tailwind CSS

## Estrutura
- `src/app`: rotas e páginas
- `src/components`: componentes reutilizáveis
- `src/lib`: cliente HTTP e tipos
- `src/types`: tipos compartilhados

## Arquitetura e padrões
- App Router com organização por rotas e componentes.
- Uso de Server Components e Server Actions para operações no servidor.
- Separação entre UI, cliente HTTP e tipagens compartilhadas.
- Tipagem centralizada para integração com a API do back-end.

## Requisitos
- Node.js (versão LTS recomendada)
- npm

## Variáveis
Copie `.env.local.example` para `.env.local`.

## Docker: quando precisa
- O front-end pode rodar sem Docker.
- Docker é recomendado quando você quiser subir o ambiente completo do projeto (dependências compartilhadas).

## Executar (local, sem Docker)
- `make install`
- `make dev`

## Executar (com Docker no projeto)
- Suba os serviços do ambiente na raiz do monorepo com `docker compose up -d`.
- Depois, inicie o front-end com `make dev`.
