import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/services/session.service';
import { HallService } from '../../core/services/hall.service';
import { SpeakerService } from '../../core/services/speaker.service';
import { Session, CreateSessionRequest } from '../../core/models/session.model';
import { Hall } from '../../core/models/hall.model';
import { Speaker } from '../../core/models/speaker.model';

@Component({
  selector: 'app-session-management',
  imports: [RouterLink, CommonModule, FormsModule],
  templateUrl: './session-management.component.html',
  styleUrl: './session-management.component.scss'
})
export class SessionManagementComponent implements OnInit {
  eventId!: number;
  sessions: Session[] = [];
  halls: Hall[] = [];
  speakers: Speaker[] = [];
  loading = true;
  error = '';
  formError = '';
  showForm = false;

  form: CreateSessionRequest = {
    title: '',
    description: '',
    startTime: '',
    endTime: '',
    hallId: null,
    speakerId: null
  };

  speakerSearch = '';
  showSpeakerDropdown = false;
  editingId: number | null = null;

  constructor(
    private route: ActivatedRoute,
    private sessionService: SessionService,
    private hallService: HallService,
    private speakerService: SpeakerService
  ) {}

  ngOnInit() {
    this.eventId = Number(this.route.snapshot.paramMap.get('id'));
    this.loadSessions();
    this.loadHalls();
    this.loadSpeakers();
  }

  loadSpeakers() {
    this.speakerService.getAllSpeakers().subscribe({
      next: (data) => this.speakers = data,
      error: () => {}
    });
  }

  get filteredSpeakers(): Speaker[] {
    const term = this.speakerSearch.trim().toLowerCase();
    if (!term) return this.speakers;
    return this.speakers.filter(s =>
      s.name.toLowerCase().includes(term) ||
      (s.organization ? s.organization.toLowerCase().includes(term) : false)
    );
  }

  onSpeakerInput() {
    this.showSpeakerDropdown = true;
    this.form.speakerId = null; // typing invalidates a previous pick until re-selected
  }

  selectSpeaker(speaker: Speaker) {
    this.form.speakerId = speaker.id;
    this.speakerSearch = speaker.name;
    this.showSpeakerDropdown = false;
  }

  clearSpeaker() {
    this.form.speakerId = null;
    this.speakerSearch = '';
    this.showSpeakerDropdown = false;
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

  getHallName(hallId: number): string {
    const hall = this.halls.find(h => h.id === hallId);
    return hall ? hall.name : 'Unknown Hall';
  }

  openCreateForm() {
    this.editingId = null;
    this.resetForm();
    this.showForm = true;
  }

  editSession(session: Session) {
    this.editingId = session.id;
    this.form = {
      title: session.title,
      description: session.description || '',
      startTime: session.startTime ? session.startTime.slice(0, 16) : '',
      endTime: session.endTime ? session.endTime.slice(0, 16) : '',
      hallId: session.hallId,
      speakerId: session.speakerId ?? null
    };
    this.speakerSearch = session.speakerName || '';
    this.formError = '';
    this.showForm = true;
  }

  cancelForm() {
    this.showForm = false;
    this.editingId = null;
    this.resetForm();
  }

  private resetForm() {
    this.form = { title: '', description: '', startTime: '', endTime: '', hallId: null, speakerId: null };
    this.speakerSearch = '';
    this.formError = '';
  }

  onSubmit() {
    const payload = {
      ...this.form,
      startTime: this.form.startTime + ':00',
      endTime: this.form.endTime + ':00'
    };

    if (this.editingId) {
      this.sessionService.updateSession(this.editingId, payload).subscribe({
        next: (updated) => {
          this.sessions = this.sessions.map(s => s.id === this.editingId ? updated : s);
          this.cancelForm();
        },
        error: () => this.formError = 'Failed to update session.'
      });
    } else {
      this.sessionService.createSession(this.eventId, payload).subscribe({
        next: (session) => {
          this.sessions.push(session);
          this.cancelForm();
        },
        error: () => this.formError = 'Failed to create session.'
      });
    }
  }

  deleteSession(sessionId: number) {
    if (!confirm('Delete this session?')) return;
    this.sessionService.deleteSession(sessionId).subscribe({
      next: () => {
        this.sessions = this.sessions.filter(s => s.id !== sessionId);
      },
      error: () => alert('Failed to delete session.')
    });
  }
}
