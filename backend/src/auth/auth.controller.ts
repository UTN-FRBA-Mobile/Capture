import {
  Controller,
  Post,
  Req,
  UnauthorizedException,
  UseGuards,
} from '@nestjs/common';
import { AuthService } from './auth.service';
import { LocalAuthGuard } from './guards/localAuth.guard';
import { JwtAuthGuard } from './guards/jwtAuth.guard';
import { User } from '../common/decorators/user.decorator';
import { JwtPayload } from './interfaces/jwtPayload.interface';

@Controller('auth')
export class AuthController {
  constructor(private readonly authService: AuthService) {}

  @UseGuards(LocalAuthGuard)
  @Post('login')
  login(@Req() request) {
    return this.authService.login(request.user);
  }

  @UseGuards(JwtAuthGuard)
  @Post('valid')
  async valid(@User() user: JwtPayload | undefined) {
    if (!user || !(await this.authService.userExists(user.username))) {
      throw new UnauthorizedException();
    }
  }
}
