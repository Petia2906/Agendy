import { Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { SpeakerService } from '../../core/services/speaker.service';
import { Speaker, CreateSpeakerRequest } from '../../core/models/speaker.model';
import { HeaderComponent } from '../../shared/header/header.component';

@Component({
  selector: 'app-speaker-management',
  imports: [RouterLink, CommonModule, FormsModule, HeaderComponent],
  templateUrl: './speaker-management.component.html',
  styleUrl: './speaker-management.component.scss'
})
export class SpeakerManagementComponent implements OnInit {
  speakers: Speaker[] = [];
  loading = true;
  error = '';

  form: CreateSpeakerRequest = {
    name: '',
    email: '',
    password: '',
    bio: '',
    photoUrl: '',
    organization: ''
  };

  formError = '';
  showForm = false;
  editingId: number | null = null;

  constructor(private speakerService: SpeakerService) {}

  ngOnInit() {
    this.loadSpeakers();
  }

  loadSpeakers() {
    this.speakerService.getAllSpeakers().subscribe({
      next: (data) => {
        this.speakers = data;
        this.loading = false;
      },
      error: () => {
        this.error = 'Failed to load speakers.';
        this.loading = false;
      }
    });
  }

  openCreateForm() {
    this.editingId = null;
    this.form = { name: '', email: '', password: '', bio: '', photoUrl: '', organization: '' };
    this.formError = '';
    this.showForm = true;
  }

  editSpeaker(speaker: Speaker) {
    this.editingId = speaker.id;
    this.form = {
      name: speaker.name,
      email: speaker.email,
      password: '',
      bio: speaker.bio || '',
      photoUrl: speaker.photoUrl || '',
      organization: speaker.organization || ''
    };
    this.formError = '';
    this.showForm = true;
  }

  cancelForm() {
    this.showForm = false;
    this.editingId = null;
    this.form = { name: '', email: '', password: '', bio: '', photoUrl: '', organization: '' };
    this.formError = '';
  }

  onSubmit() {
    if (this.editingId) {
      this.speakerService.updateSpeaker(this.editingId, {
        name: this.form.name,
        bio: this.form.bio,
        photoUrl: this.form.photoUrl,
        organization: this.form.organization
      }).subscribe({
        next: (updated) => {
          this.speakers = this.speakers.map(s => s.id === this.editingId ? updated : s);
          this.cancelForm();
        },
        error: (err) => {
          this.formError = err.error?.message || err.error || 'Failed to update speaker.';
        }
      });
    } else {
      this.speakerService.createSpeaker(this.form).subscribe({
        next: (speaker) => {
          this.speakers.push(speaker);
          this.cancelForm();
        },
        error: (err) => {
          this.formError = err.error?.message || err.error || 'Failed to create speaker.';
        }
      });
    }
  }
}
