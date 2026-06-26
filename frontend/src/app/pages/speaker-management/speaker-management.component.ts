import { Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { SpeakerService } from '../../core/services/speaker.service';
import { Speaker, CreateSpeakerRequest } from '../../core/models/speaker.model';

@Component({
  selector: 'app-speaker-management',
  imports: [RouterLink, CommonModule, FormsModule],
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

  onSubmit() {
    this.speakerService.createSpeaker(this.form).subscribe({
      next: (speaker) => {
        this.speakers.push(speaker);
        this.form = { name: '', email: '', password: '', bio: '', photoUrl: '', organization: '' };
        this.showForm = false;
        this.formError = '';
      },
      error: (err) => {
        this.formError = err.error?.message || err.error || 'Failed to create speaker.';
      }
    });
  }
}
