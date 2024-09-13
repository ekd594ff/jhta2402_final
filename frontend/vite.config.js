import {defineConfig, loadEnv} from 'vite'
import react from '@vitejs/plugin-react-swc'

// https://vitejs.dev/config/
export default ({mode}) => {
    const env = loadEnv(mode, "../");
    return defineConfig({
        plugins: [react()],
        build: {
            outDir: "../src/main/resources/static"
        },
        server: {
            port: parseInt(env.VITE_REACT_DEV_PORT) || 8082, // .env 설정 없으면 vite 서버 8082 포트에서 실행
            proxy: {
                '/api': {
                    target: `${env.VITE_API_HOST || ""}`
                },
                '/oauth2': {
                    target: `${env.VITE_API_HOST || ""}`
                }
            }
        }
    })
}
