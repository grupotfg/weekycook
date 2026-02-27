export function resolveImagePath(
  raw: string | null | undefined,
  fallback?: string,
): string | undefined {
  if (!raw) {
    return fallback;
  }

  let path = raw.trim();
  if (!path) {
    return fallback;
  }

  path = path.replace(/\\/g, '/');
  const lower = path.toLowerCase();

  if (lower.startsWith('http://') || lower.startsWith('https://')) {
    return path;
  }

  const publicPrefix = 'public/';
  if (lower.startsWith(publicPrefix)) {
    path = path.substring(publicPrefix.length);
  }

  if (path.startsWith('./')) {
    path = path.substring(2);
  }

  const assetsPrefix = 'assets/';
  if (path.toLowerCase().startsWith(assetsPrefix)) {
    return path;
  }

  if (path.startsWith('/')) {
    return path;
  }

  path = path.replace(/^\/+/, '');
  return `/${path}`;
}
