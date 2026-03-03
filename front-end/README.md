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

## Keycloakify (tema de login do Keycloak)

Este projeto usa um workspace dedicado para o tema de login em `keycloakify-login-theme`.

Fluxo recomendado (baseado no quick start oficial):

1. Instalar dependências do tema:
	- `npm run kc:install`
2. Iterar visualmente com Storybook:
	- `npm run kc:storybook`
3. Gerar e publicar o tema para o Keycloak local:
	- `npm run kc:deploy`

O comando `kc:deploy` gera o jar do Keycloakify e copia para:
- `../back-end/docker/keycloak/providers/craftpg-keycloak-theme.jar`

Com o `docker-compose` da raiz em execução, reinicie o container do Keycloak para recarregar providers:
- `docker compose restart keycloak`

Depois, em Keycloak Admin (`http://localhost:8081`):
- Realm Settings → Themes → Login Theme → selecione `keycloakify-starter`.

Obs.: o arquivo `back-end/docker/keycloak/realm-craftpg.json` já está configurado com `loginTheme: keycloakify-starter` para novos imports de realm.
