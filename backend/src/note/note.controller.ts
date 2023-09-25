import { Body, Controller, Get, Post, UseGuards } from '@nestjs/common';
import { NoteService } from './note.service';
import { NewNoteDto } from './dto/newNote.dto';
import { JwtAuthGuard } from '../auth/guards/jwtAuth.guard';
import { JwtPayload } from '../auth/interfaces/jwtPayload.interface';
import { User } from '../common/decorators/user.decorator';

@UseGuards(JwtAuthGuard)
@Controller('note')
export class NoteController {
  constructor(private noteService: NoteService) {}

  @Post('create')
  async create(@User() jwtUser: JwtPayload, @Body() newNoteDto: NewNoteDto) {
    return this.noteService.create(jwtUser.username, newNoteDto);
  }

  @Get('')
  async fetchAllForUser(@User() jwtUser: JwtPayload) {
    return this.noteService.fetchAllForUser(jwtUser.username);
  }
}
