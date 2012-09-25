=begin
This file is part of SSID.

SSID is free software: you can redistribute it and/or modify
it under the terms of the GNU Lesser General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

SSID is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License
along with SSID.  If not, see <http://www.gnu.org/licenses/>.
=end

class UserCourseMembership < ActiveRecord::Base
  # UserCourseMembership#role Constants
  ROLE_TEACHING_STAFF = 0
  ROLE_TEACHING_ASSISTANT = 1
  ROLE_STUDENT = 2

  belongs_to :user
  belongs_to :course
end