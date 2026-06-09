import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/services/session.service';
import { HallService } from '../../core/services/hall.service';
import { Session, CreateSessionRequest } from '../../core/models/session.model';
import { Hall } from '../../core/models/hall.model';

@Component({
  selector: 'app-session-management',
  imports: [RouterLink, CommonModule, FormsModule],
  templateUrl: './session-management.component.html',
  styleUrl: './session-management.component.scss'
})
export class SessionManagementComponent implements OnInit {
  eventId: string = '';
  sessions: Session[] = [];
  halls: Hall[] = [];
  loading = true;
  error = '';
  formError = '';
  showForm = false;

  form: CreateSessionRequest = {
    title: '',
    description: '',
    startTime: '',
    endTime: '',
    hallId: '',
    speakerId: ''
  };

  constructor(
    private route: ActivatedRoute,
    private sessionService: SessionService,
    private hallService: HallService
  ) {}

  ngOnInit() {
    this.eventId = this.route.snapshot.paramMap.get('id')!;
    this.loadSessions();
    this.loadHalls();
  }

  loadSessions() {
    this.sessionService.getSessionsByEvent(this.eventId).subscribe({
      next: (data) => {
        this.sessions = data;
        this.loading = false;
      },
      error: () => {
        this.error = 'Failed to load sessions.';
        this.loading = false;
      }
    });
  }

  loadHalls() {
    this.hallService.getHallsByEvent(this.eventId).subscribe({
      next: (data) => this.halls = data,
      error: () => {}
    });
  }

  getHallName(hallId: string): string {
    const hall = this.halls.find(h => h.id === hallId);
    return hall ? hall.name : 'Unknown Hall';
  }

  onSubmit() {
    const payload = {
      ...this.form,
      startTime: this.form.startTime + ':00',
      endTime: this.form.endTime + ':00'
    };

    this.sessionService.createSession(this.eventId, payload).subscribe({
      next: (session) => {
        this.sessions.push(session);
        this.form = { title: '', description: '', startTime: '', endTime: '', hallId: '', speakerId: '' };
        this.showForm = false;
        this.formError = '';
      },
      error: () => {
        this.formError = 'Failed to create session.';
      }
    });
  }

  deleteSession(sessionId: string) {
    if (!confirm('Delete this session?')) return;
    this.sessionService.deleteSession(sessionId).subscribe({
      next: () => {
        this.sessions = this.sessions.filter(s => s.id !== sessionId);
      },
      error: () => alert('Failed to delete session.')
    });
  }
}
