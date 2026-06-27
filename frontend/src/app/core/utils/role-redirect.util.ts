export function homeRouteForRole(role: string): string {
  switch (role) {
    case 'ADMIN': return '/dashboard';
    case 'ATTENDEE': return '/events';
    case 'SPEAKER': return '/speaker/events';
    default: return '/login';
  }
}
