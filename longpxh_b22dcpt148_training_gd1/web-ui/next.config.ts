import type { NextConfig } from "next";

const nextConfig: NextConfig = {
    transpilePackages: [
        '@mui/material',
        '@mui/system',
        '@mui/icons-material',
        '@toolpad/core'
    ],
};

export default nextConfig;
