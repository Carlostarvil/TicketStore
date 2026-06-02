import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import tailwindcss from '@tailwindcss/vite'

// Configuración del empaquetador Vite
export default defineConfig({
  plugins: [
    react(),
    tailwindcss(),
  ],
})