import React, { useState } from 'react';
import axios from 'axios';

export default function Login({ onLoginSuccess }) {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [email, setEmail] = useState(''); 
    
    const [esRegistro, setEsRegistro] = useState(false); 
    const [mensaje, setMensaje] = useState('');

    const manejarEnvio = async (e) => {
        e.preventDefault();
        setMensaje('');

        if (esRegistro) {
            try {
                const respuesta = await axios.post('http://localhost:8081/api/auth/register', {
                    username: username,
                    password: password,
                    email: email,
                    rol: "USER" 
                });
                setMensaje(`éxito:${respuesta.data.mensaje}`);
                localStorage.setItem('email', respuesta.data.email);
                setEsRegistro(false); 
                setPassword(''); 
            } catch (err) {
                if (err.response && err.response.data && err.response.data.mensaje) {
                    setMensaje(`error:${err.response.data.mensaje}`);
                } else {
                    setMensaje('error:Error de conexión con el servidor.');
                }
            }
        } else {
            try {
                const respuesta = await axios.post('http://localhost:8081/api/auth/login', {
                    username: username,
                    password: password
                });
                
                const token = respuesta.data.jwt;
                const rol = respuesta.data.rol; 
                const emailLogueado = respuesta.data.email; 
                
                localStorage.setItem('token', token);
                localStorage.setItem('rol', rol); 
                localStorage.setItem('email', emailLogueado);

                onLoginSuccess();
            } catch (err) {
                if (err.response && err.response.data && err.response.data.mensaje) {
                    setMensaje(`error:${err.response.data.mensaje}`);
                } else {
                    setMensaje('error:Credenciales incorrectas.');
                }
            }
        }
    };

    return (
        <div className="min-h-screen flex items-center justify-center p-4 font-sans text-slate-800 bg-gradient-to-br from-slate-50 via-white to-slate-100 relative overflow-hidden">
            
            {}
            <div className="absolute top-[-10%] left-[-10%] w-[40vw] h-[40vw] rounded-full bg-indigo-300/30 blur-[100px] pointer-events-none"></div>
            <div className="absolute bottom-[-10%] right-[-10%] w-[50vw] h-[50vw] rounded-full bg-cyan-300/20 blur-[100px] pointer-events-none"></div>

            <div className="w-full max-w-md bg-white/80 backdrop-blur-2xl rounded-3xl border border-white p-8 shadow-2xl relative z-10 transition-all">
                
                <div className="text-center mb-8">
                    <div className="inline-flex h-14 w-14 rounded-2xl bg-gradient-to-tr from-indigo-600 to-cyan-500 items-center justify-center shadow-lg shadow-indigo-200 mb-4">
                        <span className="text-white font-black text-2xl">T</span>
                    </div>
                    <h2 className="text-3xl font-extrabold text-slate-900 tracking-tight">
                        {esRegistro ? 'Crear cuenta' : 'Bienvenido'}
                    </h2>
                    <p className="text-sm text-slate-500 mt-2 font-medium">
                        {esRegistro ? 'Únete para conseguir tus tickets' : 'Accede a tu panel de Ticketmaster'}
                    </p>
                </div>

                {mensaje && (
                    <div className={`p-4 mb-6 rounded-2xl text-sm font-bold text-center shadow-sm ${mensaje.startsWith('éxito') ? 'bg-emerald-50 text-emerald-700 border border-emerald-100' : 'bg-rose-50 text-rose-700 border border-rose-100'}`}>
                        {mensaje.split(':')[1]}
                    </div>
                )}

                <form onSubmit={manejarEnvio} className="space-y-5">
                    <div>
                        <label className="block text-xs font-bold uppercase text-slate-400 mb-2 tracking-wider">Usuario</label>
                        <input type="text" value={username} onChange={(e) => setUsername(e.target.value)} className="w-full px-4 py-3 bg-slate-50/50 border border-slate-200 rounded-2xl text-sm focus:outline-none focus:ring-2 focus:ring-indigo-500/20 focus:border-indigo-500 focus:bg-white transition-all" required />
                    </div>

                    {esRegistro && (
                        <div>
                            <label className="block text-xs font-bold uppercase text-slate-400 mb-2 tracking-wider">Correo Electrónico</label>
                            <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} placeholder="tu@email.com" className="w-full px-4 py-3 bg-slate-50/50 border border-slate-200 rounded-2xl text-sm focus:outline-none focus:ring-2 focus:ring-indigo-500/20 focus:border-indigo-500 focus:bg-white transition-all" required />
                        </div>
                    )}

                    <div>
                        <label className="block text-xs font-bold uppercase text-slate-400 mb-2 tracking-wider">Contraseña</label>
                        <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} className="w-full px-4 py-3 bg-slate-50/50 border border-slate-200 rounded-2xl text-sm focus:outline-none focus:ring-2 focus:ring-indigo-500/20 focus:border-indigo-500 focus:bg-white transition-all" required />
                    </div>

                    <button type="submit" className="w-full py-3.5 px-4 bg-slate-900 hover:bg-indigo-600 text-white font-bold text-sm rounded-2xl shadow-lg shadow-slate-200 hover:shadow-indigo-200 transition-all active:scale-95 uppercase tracking-wider mt-4">
                        {esRegistro ? 'Registrarse' : 'Iniciar Sesión'}
                    </button>
                </form>

                <div className="mt-8 text-center">
                    <button type="button" onClick={() => { setEsRegistro(!esRegistro); setMensaje(''); }} className="text-sm font-bold text-slate-500 hover:text-indigo-600 transition-colors">
                        {esRegistro ? '¿Ya tienes cuenta? Inicia sesión' : '¿No tienes cuenta? Regístrate'}
                    </button>
                </div>
            </div>

            {}
            <footer className="absolute bottom-6 left-0 right-0 text-center text-[10px] font-bold text-slate-400 tracking-widest uppercase pointer-events-none z-10">
                © 2026 Ticketmaster API · Panel de Acceso Seguro
            </footer>
        </div>
    );
}